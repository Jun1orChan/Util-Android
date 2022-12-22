# Util-Android

## 引用


1.项目根目录build.gradle文件中添加

```gradle
    maven { url 'https://jitpack.io' }
```
2.模块build.gradle文件中添加

```gradle
    implementation 'com.github.Jun1orChan:Util-Android:1.2.1'
 ```


## 版本记录

### 1.2.1

- 新增UriUtil

### 1.2.0

- 适配Android 12
- 依赖库改为编译期间依赖

### 1.1.1

- 修复StatusBarUtil在Android11上获取状态栏错误的bug

### 1.1.0

- 新增DrawableUtil、ReflectUtil
- 修复StatusBarUtil在Android11上获取不到状态栏高度的问题

### 1.0.1

- targetVersion改为30

### 1.0.0

- 初次发布