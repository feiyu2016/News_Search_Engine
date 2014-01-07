@echo off 
CD %CATALINA_HOME%/webapps/ROOT/WEB-INF/classes
echo "Crawling news from sohu ..."
::crawl data
call java -classpath "../lib/htmlparser.jar;../lib/mysql-connector-java-5.1.6-bin.jar;" com.crawler.CrawlerSohu
call java -classpath "../lib/htmlparser.jar;../lib/mysql-connector-java-5.1.6-bin.jar;" com.crawler.Crawler163
call java -classpath "../lib/htmlparser.jar;../lib/mysql-connector-java-5.1.6-bin.jar;" com.crawler.CrawlerSina

::delete the temp file
if not exist E:/indextmp goto copyindex
call del /Q /S E:/indextmp
call rmdir /Q /S E:/indextmp

:copyindex
CD E:/
echo %cd%
mkdir indextmp
xcopy /s /y index indextmp

::adding index
CD %CATALINA_HOME%/webapps/ROOT/WEB-INF/classes
call java -classpath "../lib/lucene-core-2.3.1.jar;../lib/mysql-connector-java-5.1.6-bin.jar;../lib/commons-logging-1.1.1.jar;../lib/paoding-analysis.jar;" com.lucene.addIndex E:/indextmp
CD %CATALINA_HOME%/bin

call shutdown.bat
ping -n 10 127.0.0.1
::del backup index
CD E:/
if not exist indexold goto copy_rename
call del /Q /S indexold
call rmdir /Q /S indexold
:copy_rename
call rename index indexold
call rename indextmp index
CD %CATALINA_HOME%/bin
call startup.bat
