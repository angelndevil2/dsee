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

## TODO

* manual
* bci to monitor WAS service
* refer to wiki or intro web

```
    Copyright 2016 angelndevil2
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
```
