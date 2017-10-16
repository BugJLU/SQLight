
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# If using SEE, uncomment the following:
# LOCAL_CFLAGS += -DSQLITE_HAS_CODEC

#Define HAVE_USLEEP, otherwise ALL sleep() calls take at least 1000ms
LOCAL_CFLAGS += -DHAVE_USLEEP=1

# Enable SQLite extensions.
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS5 
LOCAL_CFLAGS += -DSQLITE_ENABLE_RTREE
LOCAL_CFLAGS += -DSQLITE_ENABLE_JSON1
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS3

# Enable SQLite funtions.
LOCAL_CFLAGS += -DINSERT_FUNC
LOCAL_CFLAGS += -DEXPR_FUNC
LOCAL_CFLAGS += -DAUTH_FUNC
LOCAL_CFLAGS += -DANALYZE_FUNC
#LOCAL_CFLAGS += -DALTER_FUNC
LOCAL_CFLAGS += -DBUILD_FUNC
LOCAL_CFLAGS += -DFUNC_FUNC
LOCAL_CFLAGS += -DDATE_FUNC
LOCAL_CFLAGS += -DSELECT_FUNC
LOCAL_CFLAGS += -DWHERE_FUNC
LOCAL_CFLAGS += -DVACUUM_FUNC
LOCAL_CFLAGS += -DPRAGMA_FUNC
LOCAL_CFLAGS += -DATTACH_FUNC
LOCAL_CFLAGS += -DTRIGGER_FUNC
LOCAL_CFLAGS += -DDELETE_FUNC
LOCAL_CFLAGS += -DUPDATE_FUNC

# This is important - it causes SQLite to use memory for temp files. Since 
# Android has no globally writable temp directory, if this is not defined the
# application throws an exception when it tries to create a temp file.
#
LOCAL_CFLAGS += -DSQLITE_TEMP_STORE=3

# Fix for undeclared identifier mmap.
# from https://github.com/android-ndk/ndk/issues/332
LOCAL_CFLAGS += -D_FILE_OFFSET_BITS=32

LOCAL_CFLAGS += -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
LOCAL_CFLAGS += -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
LOCAL_CFLAGS += -U__APPLE__
LOCAL_CFLAGS += -DHAVE_STRCHRNUL=0
LOCAL_CFLAGS += -DSQLITE_USE_URI=1
LOCAL_CFLAGS += -Wno-unused-parameter -Wno-int-to-pointer-cast
LOCAL_CFLAGS += -Wno-uninitialized -Wno-parentheses
LOCAL_CPPFLAGS += -Wno-conversion-null


ifeq ($(TARGET_ARCH), arm)
	LOCAL_CFLAGS += -DPACKED="__attribute__ ((packed))"
else
	LOCAL_CFLAGS += -DPACKED=""
endif

LOCAL_SRC_FILES:=                             \
	android_database_SQLiteCommon.cpp     \
	android_database_SQLiteConnection.cpp \
	android_database_SQLiteGlobal.cpp     \
	android_database_SQLiteDebug.cpp      \
	JNIHelp.cpp JniConstants.cpp

LOCAL_SRC_FILES += sqlite3.c

LOCAL_C_INCLUDES += $(LOCAL_PATH) $(LOCAL_PATH)/nativehelper/

LOCAL_MODULE:= libsqliteX
LOCAL_LDLIBS += -ldl -llog 

include $(BUILD_SHARED_LIBRARY)

