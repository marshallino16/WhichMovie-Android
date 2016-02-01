# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/genyus/Documents/sdk/tools/proguard/proguard-android.txt
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

-injars bin/classes
-injars libs
-outjars bin/classes-processed.jar
-libraryjars /Users/GENyUS/Documents/sdk/platforms/android-23/android.jar

-optimizations !code/simplification/arithmetic
-dontnote
-repackageclasses ''
-allowaccessmodification
-dontskipnonpubliclibraryclasses
-ignorewarnings
-keepattributes *Annotation*,EnclosingMethod
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-dontskipnonpubliclibraryclassmembers

#Configuration de base Android:
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# will keep line numbers and file name obfuscation
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# -optimizations !code/allocation/variable
# Appsee recommandations. Replace with above lines if doesn't fix the issue
-optimizations !code/*,!field/*,!class/merging/*,!method/*

-dontwarn javax.mail.**
-dontwarn javax.management.**
-dontwarn javax.rmi.**
-dontwarn javax.naming.**
-dontwarn javax.transaction.**
-dontwarn javax.persistence.**
-dontwarn java.lang.management.**
-dontwarn java.lang.instrument.**
-dontwarn org.slf4j.**
-dontwarn org.json.**

-keep class org.apache.**
-dontwarn org.apache.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-dontwarn org.springframework.**

-dontwarn sun.misc.Unsafe
-dontwarn com.google.gwt.**

# We shouldn't be needing this since AppCompat has renamed the MenuBuilder class from 23.1.1
#
# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
# -keep class !android.support.v7.internal.view.menu.*MenuBuilder*, android.support.v7.** { *; }
# -keep interface android.support.v7.** { *; }

# Configuration for Fabric Twitter Kit
# See: https://dev.twitter.com/twitter-kit/android/integrate

-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* *;
}


# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes EnclosingMethod


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


# Branch
-keep class com.google.android.gms.ads.identifier.** { *; }


# OkHttp
-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-dontwarn com.squareup.leakcanary.DisplayLeakService


# Intercom
-dontwarn intercom.**
-dontwarn io.intercom.**


# Eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# Appsee
-keep class com.appsee.** { *; }
-dontwarn com.appsee.**


-keep class com.google.appengine.api.datastore.Text { *; }

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

-keepclassmembers class * extends android.content.Context {
 public void *(android.view.View);
 public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
 static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

#Pour supprimer les logs lors de la compilation
-assumenosideeffects class android.util.Log {
public static int v(...);
public static int d(...);
public static int i(...);
public static int w(...);
public static int e(...);
public static int wtf(...);
}

#Pour que proguard ignore toutes les classes contenues dans un certain package
-keep class genyus.com.whichmovie.** { *; }
-dontwarn org.**


#Pour ActionBarSherlock (d'aprèes le site officiel)
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
