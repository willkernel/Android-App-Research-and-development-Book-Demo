# Practice


1. AndroidLib
业务无关公用代码
activity BaseActivity，公用逻辑
net 网络层
cache 缓存数据，图片相关处理
ui 自定义控件
utils（SharedPreferences）


2.项目包模块
activity,adapter,entity
db SQLLite 相关逻辑的封装
engine 业务相关类
interfaces 接口,命名以I作为开头
listener  基于Listener接口，命名以On作为开头


3.Activity新的生命周期，统一事件编程模型
solid原则，单一职责，一个类，一个方法只做一件事
BaseActivity 子类实现它的抽象方法
Button setOnClickListener，提取出私有方法


4.实体化编程FastJson，GSON
有符号Annotation实体属性，泛型属性，使用崩溃
需要在混淆文件添加
-keepattributes Signature //避免混淆泛型
-keepattributes *Annotation* //不混淆注解


5.类型安全转换函数
convertInt(Object value,int default);
转换失败，返回默认值 ，其他long，String类型同理
substring(),长度不够，崩溃。判断是否越界，ex: ength>1



抛弃AsyncTask，自定义网络底层封装框架
设计一套App缓存策略
设计一套MockService，模拟网络返回数据
封装用户Cookie逻辑

1.JSON返回Response公用属性，可封装一个基类
2.网络底层封装在AndroidLib库中

使用原生ThreadPoolExecutor+Runnable+Handler

网络底层优化
1.onFail统一处理
2.UrlConfigManager优化
3.不是每个请求都需要回调的
4.ProgressBar 的处理


数据缓存处理
保存在SD卡，数据库中
POST请求，GET 请求，请求URL作为键值，排序算法对URL中的key进行排序，检查是否有缓存数据，没有直接获取
如果有缓存数据，与缓存数据的Expired,Version比较是否需要更新
