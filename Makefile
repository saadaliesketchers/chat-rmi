JC = javac

# make already understands basic things about application development in C: 
# for example, that  object files have a .o suffix, sourcecode files have a .c suffix, 
# and the way to get the first from the second is to run the C compiler, cc, with appropriate arguments.  
# make does not know the corresponding things about .java and .class files and the Java compiler javac.  
# But it can easily be told about them. 
# That tells make to add .java and .class to the list of filename suffixes that it knows about:
.SUFFIXES: .java .class

#
# You still need to tell it what to do with files with those suffixes:  
# That tells make that any file with a .class suffix depends on a file with the same name but a .java suffix, 
# and the way to get the first from the second is to run javac with the depended-on file (the .java file) as argument.  
# If you want to always pass certain flags to javac, you can put them in here; it's just a command line that will be forked in a new shell.
#
.java.class:
	$(JC) $<
# The same as $(JC) $*.java
#

CLASSES = \
        ChatServer.java \
        ChatServerImplement.java \
        ChatClient.java \
        ChatClientImplement.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
