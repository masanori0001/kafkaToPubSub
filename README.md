# 概要
- KafkaからGCP pub/subへ書き込みを行うプログラム
- 可用性を保証したリアルタイムストリーミング処理

# アーキテクチャ
- beam on flink on yarn

# 事前条件
- Hadoop yarnの構築
- Flinkの構築

# Flinkのyarn-sessionの起動方法
- アプリケーションの実行前にyarn-sessionを起動する。起動時にはyarn.taskmanager.envを指定して、環境変数を定義する。GOOGLE_APPLICATION_CREDENTIALSでgoogle credentialを指定する。
```
./bin/yarn-session.sh -n 4 -jm 1024 -tm 4096 -D yarn.taskmanager.env.GOOGLE_APPLICATION_CREDENTIALS=/home/hadoop/.google_credentials  
```
