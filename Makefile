JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        AIModule.java \
        alphabeta_MoodyCasias.java \
        GameController.java \
	GameState_General.java \
	GameState_Opt7x6.java \
	GameStateModule.java \
	IOModule.java \
	TextDisplay.java \
        Main.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
