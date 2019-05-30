# backup_qiniu
获取七牛文件列表,并备份到指定目录


1.在根目录执行:mvn clean install
2.在根目录/target目录下执行 
java -DaccessKey=vUlz_retu6yaHqyQZ3W1PTEymdc5giLwRRMjFF1E -DsecretKey=OukY-e1LMACPJvEO_aLU2A7cneq_0MbQ7HycYi9W -Dbucket=blog -DvisitHost=http://cdn.tonfay.cn/ -DsavePath=/tmp -jar target/backup-jar-with-dependencies.jar

accessKey   
secretKey   
bucket  
visitHost   公网访问域名
savePath    本地保存路径
