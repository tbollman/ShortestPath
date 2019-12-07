#Makefile for CSCE416 La5

COMPILE = javac

default: lsrouter.java dvrouter.java
	$(COMPILE) lsrouter.java dvrouter.java

lsrouter.class: lsrouter.java
	$(COMPILE) lsrouter.java
	
dvrouter.class: dvrouter.java
	$(COMPILE) dvrouter.java
	
clean:
	rm -rf *.class
	
