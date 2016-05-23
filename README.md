# DSee Agent

jvm inspection agent

## Agent Load

### Load when jvm start

```
use -javaagent:/path/to/dsee/dsee-version-bootstrap.jar
```
### Load to running jvm

can be used with HOTSPOT jvm

* check running jvm's ___pid___
* edit /path/to/dsee/bin/launcher
    * export DSEE_OPTS=-Xbootclasspath/a:<path_to_jdk>/lib/tools.jar
* command
```
/path/to/dsee/bin/launcher -p pid
```

## agent.properties

* file :  __/path/to/dsee/conf/agent.properties__
* turn off logback log __logback.use=false__

## jetty.properties

* file : __/path/to/dsee/conf/jetty.properties__
* default port : 1080
    * property : http.port

## logback.xml

* file : __/path/to/dsee/conf/logback.xml__
* log file : /path/to/dsee/logs/dsee.log
* rotation : daily

## Status

* developed with weblogic 12c
* mbean developed with websphere 8.5.5.8
* not fully tested
