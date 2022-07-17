#include <jni.h>
#include <string>

//key text sudah diganti, key asset sudah diganti ke Master Jigsaw Game
std::string keyDesText = "U6g_Oyzo$F1%Hm(k";
std::string keyDesAssets = "DP3CIofsx9MYqH1uI4ihjg==";

//kode iklan sudah diganti ke master jigsaw game
std::string adBanner =              "mDwAA{E}P>_2%(94!*!%0_*!(_( %%^4W!QQ7-";
std::string adInterstitial =        "mDwAA{E}P>_2%(94!*!%0_*!(_( (&_5^^)+q8";
//std::string adNativeMenu =          "mDwAA{E}P>_2%(94!*!%0_*!(_( @)&3(*_Q09";

//startApp sudah diganti Master Jigsaw Game
std::string startAppId = "6+0$+%%)%";

//pesan eror sudah diganti Master Jigsaw Game
std::string smesek = "H{/EOJ}<K4Id`WtpNG`E[I:`V}Ur`GA]XIMFo]{.";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "us.mas";
std::string tengahP = "terjigsawgame.";
std::string ahirP = "wolfjigsawpuzzle";
std::string finalPackage = awalP + tengahP + ahirP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "JNI Failed!");
        return NULL;
    }
    return text;
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_adInterstitial(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adInterstitial.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_adBanner(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adBanner.c_str()));
}

//extern "C"
//JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_adNativeMenu(
//        JNIEnv *env, jobject, jobject activity ) {
//    return env->NewStringUTF(cekStatus(env, activity, adNativeMenu.c_str()));
//}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_us_masterjigsawgame_util_Komunikator_smesek(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, smesek.c_str()));
}
