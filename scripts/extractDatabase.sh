adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db > bitfolio.db
adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db-wal > bitfolio.db-wal
adb exec-out run-as com.zredna.bitfolio cat databases/bitfolio.db-shm > bitfolio.db-shm
sqlite3 bitfolio.db "PRAGMA wal_checkpoint"
rm bitfolio.db-wal
rm bitfolio.db-shm
open -a "DB Browser For SQLite" bitfolio.db