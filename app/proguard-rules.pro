# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wukongbao/Developer/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keep class android.support.** { *; }
-dontwarn okio.**
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**


-keep class com.atilika.**{*;}

-keepclassmembers class im.dacer.kata.core.model.** {
    !static !private <fields>;
}

#https://github.com/mikepenz/Android-Iconics
-keep class .R
-keep class **.R$* {
    <fields>;
}

#https://github.com/81813780/AVLoadingIndicatorView
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }