# XhsEmoticonsKeyboard

> j.s 🇨🇳

也许是最良心的开源表情键盘解决方案。

# Features

* API > 9 
* 表情键盘支持无闪烁自跟随系统软键盘高度,及支持自定义高度
* 表情支持自定义格式,支持任意来源
* 组件支持完全自定义,样式支持任意更改
* 支持全屏
* 默认微信键盘样式
* 赠QQ键盘高仿,不谢


# Emoji

「 [w446108264/AndroidEmoji](https://github.com/w446108264/AndroidEmoji) 」
 
## Screen Recrod

<img src="output/chat-qqemoticon.png" width="32%" /> 
<img src="output/chat-qqplug.png" width="32%" /> 
<img src="output/chat-qqfav.png" width="32%" /> 

<img src="output/chat_simple_fullscreen.png" width="32%" /> 
<img src="output/chat-bigimage.png" width="32%" /> 
<img src="output/chat-userdefui.png" width="32%" /> 


<img src="output/chat-text.png" width="32%" />
<img src="output/simple-comment.png" width="32%" /> 
<img src="output/main.png" width="32%" />  

  
# Samples APK

You can [download a sample APK](https://github.com/w446108264/XhsEmoticonsKeyboard/raw/master/output/simple.apk) 


# Gradle Dependency

Users of your library will need add the jitpack.io repository:

```xml  
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

and:

```xml
dependencies { 
    compile 'com.github.w446108264:XhsEmoticonsKeyboard:2.0.2'
}
```

# Samples Usage

```xml
<?xml version="1.0" encoding="utf-8"?>
<sj.keyboard.XhsEmoticonsKeyBoard xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/ek_bar"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:theme="@style/XhsEmoticonsKeyboardTheme.AppBarOverlay">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="?attr/colorPrimary"
                app:popupTheme="@style/XhsEmoticonsKeyboardTheme.PopupOverlay" />

        </android.support.design.widget.AppBarLayout>

        <ListView
            android:id="@+id/lv_chat"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:cacheColorHint="#00000000"
            android:divider="@null"
            android:fadingEdge="none"
            android:fitsSystemWindows="true"
            android:listSelector="#00000000"
            android:scrollbarStyle="outsideOverlay"
            android:scrollingCache="false"
            android:smoothScrollbar="true"
            android:stackFromBottom="true" />
    </LinearLayout>

</sj.keyboard.XhsEmoticonsKeyBoard>

```

```java
PageSetAdapter pageSetAdapter = new PageSetAdapter();

// add a emoticonSet
EmoticonPageSetEntity xhsPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(ParseDataUtils.ParseXhsData(DefXhsEmoticons.xhsEmoticonArray, ImageBase.Scheme.ASSETS))
                .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(getCommonEmoticonDisplayListener(emoticonClickListener, Constants.EMOTICON_CLICK_TEXT)))
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.ASSETS.toUri("xhsemoji_19.png"))
                .build();
        
pageSetAdapter.add(xhsPageSetEntity);
ekBar.setAdapter(pageSetAdapter);
```

 
 
# Simple Default Keyboard Layout Tree 「 [SVG high definition](http://www.shengjun.so/treeview.svg) 」

<img src="output/treeview.png" width="100%" /> 


# Contact & Help

Please fell free to contact me if there is any problem when using the library.

* email: shengjun8486@gmail.com 


