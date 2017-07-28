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