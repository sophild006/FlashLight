# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

-keepattributes SourceFile,LineNumberTable



-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-repackageclasses 'o'
-allowaccessmodification

-optimizationpasses 5

-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontoptimize

-dontpreverify

-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes *Annotation*

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment


-ignorewarning

-dump proguard/class_files.txt
-printseeds proguard/seeds.txt
-printusage proguard/unused.txt
-printmapping proguard/mapping.txt

-dontwarn android.support.**

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-dontwarn net.poemcode.**
-keep class android.content.* {*;}
-dontwarn android.content.**

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.forchan.forchan].R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keepnames class * implements java.io.Serializable

  -keep public abstract interface com.asqw.android.Listener{
  public protected <methods>;
  }
  -keep public class com.asqw.android{
  public void Start(java.lang.String);
  }
  -keepclasseswithmembernames class * {
  native <methods>;
  }
  -keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
  }
  -keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
  }
  -keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
  }
  -keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
  }
  -keepclasseswithmembers class org.jboss.netty.util.internal.LinkedTransferQueue {
    volatile transient org.jboss.netty.util.internal.LinkedTransferQueue$Node head;
    volatile transient org.jboss.netty.util.internal.LinkedTransferQueue$Node tail;
    volatile transient int sweepVotes;

  }

-keepclassmembers class ** {
    public void onEvent*(**);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}
-keep public abstract interface com.asqw.android.Listener{
public protected <methods>;

}


-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.project.mocha_patient.login.SignResponseData { private *; }


##---------------Begin: proguard configuration for okhttp  ----------
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.*
##---------------End: proguard configuration for okhttp  ----------

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** {*;}


-dontwarn com.koushikdutta.ion.**
-keep class  com.koushikdutta.ion.**{*;}

-keep class com.fancy.callrecorder.modules.mainscreen.widget.RecorderTabLayout {public private protected *;}

-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final *** CREATOR;
}

-keep @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <methods>;
}

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}

-dontwarn android.security.NetworkSecurityPolicy

-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}

-dontwarn com.loopme.**
-keep class com.loopme.** {
*;
}

-keep class com.solid.gamesdk.bean.** {
*;
}

-keep class com.solid.news.bean.** { *; }


-keep class com.solid.lock.bean.** {*;}

-keep class com.solid.asosdk.bean.** {*;}

-dontwarn com.applovin.**

-keep class com.applovin.** { *; }

-keep class com.google.android.gms.ads.identifier.** { *; }

-dontwarn com.vungle.**
-dontnote com.vungle.**
-keep class com.vungle.** { *; }
-keep class javax.inject.*
# ignore eventbus warnings
-dontwarn de.greenrobot.event.util.**
# ignore rx warnings
-dontwarn rx.internal.util.unsafe.**
# keep some important rx stuff - https://github.com/ReactiveX/RxJava/issues/3097
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class rx.schedulers.Schedulers { public static <methods>; }
-keep class rx.schedulers.ImmediateScheduler { public <methods>; }
-keep class rx.schedulers.TestScheduler { public <methods>; }
-keep class rx.schedulers.Schedulers { public static ** test(); }


# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}

# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}


-dontwarn com.loopme.**
-keep class com.loopme.** {
*;
}

-keepclasseswithmembers public class app.bright.flashlight.bean.** { *; }