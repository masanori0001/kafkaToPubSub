# 概要
- KafkaからBQへ書き込みを行うプログラム
- beam on flink on yarnの構成

# yarn-sessionの起動方法
./bin/yarn-session.sh -n 4 -jm 1024 -tm 4096 -D yarn.taskmanager.env.GOOGLE_APPLICATION_CREDENTIALS=/home/hadoop/.google_credentials  
yarn.taskmanager.envを指定して、環境変数を定義する必要があります
