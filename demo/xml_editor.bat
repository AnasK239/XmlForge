@echo off
mvn exec:java -q -Dexec.mainClass="com.editor.app.Main" -Dexec.args="%*"