# Practice
#### AndroidLib

1. 业务无关公用代码
activity BaseActivity，公用逻辑
net 网络层
cache 缓存数据，图片相关处理
ui 自定义控件
utils（SharedPreferences）

2. 项目包模块
activity,adapter,entity
db SQLLite 相关逻辑的封装
engine 业务相关类
interfaces 接口,命名以I作为开头
listener  基于Listener接口，命名以On作为开头
UIL,Fresco 图片加载

3. Activity新的生命周期，统一事件编程模型
solid原则，单一职责，一个类，一个方法只做一件事
BaseActivity 子类实现它的抽象方法
Button setOnClickListener，提取出私有方法

4. 实体化编程FastJson，GSON
有符号Annotation实体属性，泛型属性，使用崩溃
需要在混淆文件添加
-keepattributes Signature //避免混淆泛型
-keepattributes *Annotation* //不混淆注解

5. 类型安全转换函数
convertInt(Object value,int default);
转换失败，返回默认值 ，其他long，String类型同理
substring(),长度不够，崩溃。判断是否越界，ex: length>1

#### 简单网络框架

抛弃AsyncTask，自定义网络底层封装框架<br>
设计一套App缓存策略<br>
设计一套MockService，模拟网络返回数据<br>
封装用户Cookie逻辑

1. JSON返回Response公用属性，可封装一个基类
2. 网络底层封装在AndroidLib库中

使用原生ThreadPoolExecutor+Runnable+Handler

#### 网络底层优化

1. onFail统一处理<br>
2. UrlConfigManager优化<br>
3. 不是每个请求都需要回调的<br>
4. ProgressBar 的处理


#### 数据缓存处理

保存在SD卡，数据库中<br>
POST请求，GET请求，请求URL作为键值，排序算法对URL中的key进行排序，检查是否有缓存数据，没有直接获取.
如果有缓存数据，与缓存数据的Expired,Version比较是否需要更新

#### MockClass

1. url.xml 中配置Node节点MockClass 属性，指定使用Mock子类生成的数据
MockClass="con.xxx.xxx.MockWeatherInfo"<br>
2. 使用反射工厂设计MockService，MockService是基类，有抽象方法，getJsonData(),返回手动生成的数据
```java
public abstract class MockService{
    public abstract String getJsonData();
}
```
3. 实现反射机制

#### 安全措施

1. 需要登录的操作，跳转到登录，然后回调<br>
2. 接口请求设置Cookie机制，获取网络连接 头部中的Set-Cookie,保存到CookieManager<br>
3. 防止刷屏，同一IP访问，设置短时间内请求次数，输入验证码等操作<br>
4. 防止重定向，判读域名Host

#### 优化网络

1. gzip压缩
2. 设置服务端客户端时间 ，时区造成的时间差
3. 连接网络后对响应状态的判断
4. 设置网络请求通用头部信息
5. [Android 浅析 HttpURLConnection](https://jasonzhong.github.io/2017/01/26/Android-%E6%B5%85%E6%9E%90-HttpURLConnection)

#### 网络流量优化

- 通信层面优化 
1. 接口返回数据进行gzip压缩,大于1KB才进行压缩,否则得不偿失<br>
2. 通常数据传输遵行JSON,推荐新的传输协议,[ProtoBuf](https://developers.google.com/protocol-buffers/),这种协议是二进制的,表示大数据时,空间比JSON小很多<br>
Protocol Buffers 及[Nano-Proto-Buffers](https://github.com/nanopb/nanopb)
Protocol Buffers是Google设计的语言无关、平台无关的一种轻便高效的序列化结构数据存数格式，类似XML，但更小、更快、更简单、很适合做数据存储或者RPC数据交换的格式。它可用于通讯协议，数据存储等领域的与语言无关、平台无关，可扩展的序列化结构数据格式。
在移动端应该使用Nano-Proto-Buffers版本。因为普通的Protocol Buffers会生成非常冗长的代码，可能会增加APP内存占用，导致APK体积增长、性能下降，不注意的话，会很快遇到64K方法数限制问题。
3. 解决频繁调用API问题<br>
4. HTTP协议速度远不如TCP协议,后者是长连接,可以使用TCP提高访问速度,一台服务器支持的长连接个数不多,需要更多服务器集成<br>
5. 建立离开页面取消网络请求机制<br>
6. 增加重试机制,如果Mobile API 是严格RESTful风格,将获取数据的接口定义为GET,操作数据的接口定义为POST.这样就可以为所有GET请求配置重试机制,对POST请求增加防止用户1分钟内频繁发起相同请求的机制<br>
   在APP启动时候告诉所有API接口重试次数
   
- 图片策略优化
1. 图片URL加上 ?width=100&height=50
2. 图片单独准备服务器,http://www.imagesever.com/getImage?param=(encode_value) encode_value是对图片url,width,height进行encode,服务器进行解密,对图片进行重新绘制<br>
增加imageType(1-> 等比缩放后裁剪多余部分;2-> 等比缩放后不足的宽或者高填充白色)，有缺点，频繁读写硬盘<br>
规定宽高,如果尺寸有出入,获取面积最接近的 s=(w1-w)^2+(h1-h)^2
3. 低速网络下,降低图片质量,请求URL增加quality参数,节省流量

- 极速模式
发现当前网络为2G,3G或4G当前模式是正常模式,提示是否要进入极速模式以节省流量. 如果是WIFI网络,当前模式是极速模式，提示用户是否切换回正常模式，在设置页面也要提供这个开关，用户手动切换模式

#### 城市列表设计

1. cityId,cityName,pinyin,jianpin(全拼,简拼做字母排序和检索) 数据在不同平台的统一，便于维护，保存在本地，API获取城市列表，打开gzip压缩，增加版本号控制，及时更新，插入新的数据
2. 增量更新机制(包括增 A,删 D ,改 E) 增加一个字段,type 判断数据是哪种情况,分别处理

#### APP与HTML5

1. 基本HTML，JavaScript语法,PC 服务器搭建IIS. 在Assets内置.html页面，现实中是在远程服务器上，定好协议，APP调用JS的方法名称
```html
<script type="text/javascript">
 function changeColor(color){
    doucument.body.style.backgroundColor=color;
 }
</script>
```

```java
wv.getSettings().setJavaScriptEnabled(true);
wv.loadUrl("file:// /android_asset/104.html");
btn.setOnClickListener(...){
    ...
    String color="#00eeff";
    wv.loadUrl("javascript:changeColor('"+color+"');");
}
```
2. HTML5页面操作APP方法
```html
<a onclick="baobao.callAndroidMethod(100,100,'ccc',true)">CallAndroidMethod</a>
```
新建JSInterface1类,包括callAndroidMethod方法的实现
```java
class JSInterface1{
    public void callAndroidMethod(int a,float b,String c,boolean d){
        if(d){
            String strMsg="-"+(a+1)+"-"+(b+1)+"-"+c+"-"+d;
            new AlertDialog.Builder(this).setTitle("sss").setMessage(strMsg).show();
        }
    }
}
```
注册baobao和JSInterface1对应关系
```java
    wv.addJavaScriptInterface(new JSInterface1(),"baobao");
```
在方法前加@JavascriptInterface,否则不能触发JavaScript方法

3. APP,HTML之间定义跳转协议,实现HTML5活动页面,路由设置

#### 全局变量序列化
1. GlobalVariables implements Parcelable,Cloneable
2. 序列化本地文件缺点
  - 每次都要重新执行序列化操作
  - 序列化文件会因内存不够数据丢失
  - Android 提供的数据类型并不全部支持序列化(支持：Y;不支持：N)
  
  | 类型        | 是否支持           |
  | ------------- |:-------------:| 
  |简单类型int,String,Boolean    | Y|
  | int[],int[][], String[],String[][],Boolean[] | Y|
  | ArrayList   | Y|
  | Calendar   | Y|
  | JSONObject  | N|
  | JSONArray   | N|
  
#### 命名与编码规范
1. Java类文件
- Activity:PersonActivity
- Adapter:PersonAdapter
- Entity/Bean:PersonEntity/PersonBean
2. 资源文件
- act_person_addcustomer.xml
- item_lv_userlist.xml
- 一个页面使用资源，以页面名称做前缀
- 一个模块下多个页面使用，以模块为前缀
- 各个模块通用，common 作为前缀
- strings.xml 分模块 strings_module_a.xml
3. 编码规范
- font_size_s ,font_size_l ,font_size_xxl ,font_color_red,person_btnLogin_text
- offset_2dp,offset_4dp
- 使用style
- 不要内部嵌套类
- 不同模块实体类不共用
- 安全数据类型转换
- 尽量使用ApplicationContext代替Context
- 使用常量代替枚举
- 复杂数据保存到本地
- [CheckStyle](http://checkstyle.sourceforge.net/)

#### Crash异常收集与统计,分析
1. CrashHandler implements UncaughtExceptionHandler
2. 异常数据表结构

  | 字段        | 描述           |
  | ------------- |:-------------:| 
  |id    | 自增id|
  |client_type    | Crash所在app |
  |page_name    | Crash所在Activity |
  |exception_name    | Crash所在Activity |
  |exception_stack    | Crash详细信息 |
  |crash_type    | 1 崩溃，0 try-catch |
  |app_version    |app 版本 |
  |os_version    |Android 系统版本 |
  |device_model    |Android 手机型号 |
  |device_id    |Android 手机设备号 |
  |network_type    | 网络类型 |
  |channel_id    | 渠道号 |
  |client_type    | Android/iPhone |
  |memory_info    | 内存使用情况 |
  |crash_time    | 发生时间,数据库自动生成 |
 3. 统计，去重(数字，行号，相同信息，页面，截取前面的info)，新增规则，去除昨天重复，新增今日不同Crash，按照pageOwner 分配给不同的人，建表，执行SQL脚本，C#程序，做成自动化执行脚本
 归纳详细信息，即时性，查询Crash，趋势图
 4. 分析
 