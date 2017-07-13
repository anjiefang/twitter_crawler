This java code is used to crawl Twitter user's information (follower/followee network, description etc) by giving a list of Twitter handels. The code uses multi Twitter API keys to avoid API limits. However, I don't suggest to use too many API keys in this code.
------------------------------------------------------------------------

API keys configuration file:
------------------------------------------------------------------------

Before you use the code, you need to apply several Twitter API keys. And put the keys in API.example.config following the given format.

Usage:
------------------------------------------------------------------------
Using the following command line to compile the code:

mvn clean compile assembly:single

Type "java -jar crawler-1.0-SNAPSHOT-jar-with-dependencies.jar" to check the usage of the code.

E.g. To crawl the followers of a given handle list, use "java -jar target/crawler-1.0-SNAPSHOT-jar-with-dependencies.jar -c API.example.config -f handlelist.examples.csv -fo -o ~/"
