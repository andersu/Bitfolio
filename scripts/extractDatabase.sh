adb shell "run-as com.zredna.bitfolio chmod 666 /data/data/com.zredna.bitfolio/databases/bitfolio.db"
adb shell "run-as com.zredna.bitfolio chmod 666 /data/data/com.zredna.bitfolio/databases/bitfolio.db-wal"
adb shell "run-as com.zredna.bitfolio chmod 666 /data/data/com.zredna.bitfolio/databases/bitfolio.db-shm"
adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db > bitfolio.db
adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db-wal > bitfolio.db-wal
adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db-shm > bitfolio.db-shm
adb shell "run-as com.zredna.bitfolio chmod 600 /data/data/com.zredna.bitfolio/databases/bitfolio.db"
adb shell "run-as com.zredna.bitfolio chmod 600 /data/data/com.zredna.bitfolio/databases/bitfolio.db-wal"
adb shell "run-as com.zredna.bitfolio chmod 600 /data/data/com.zredna.bitfolio/databases/bitfolio.db-shm"
sqlite3 bitfolio.db "PRAGMA wal_checkpoint"