all:
	javac org/wiigee/event/AccelerationListener.java org/wiigee/event/ButtonListener.java org/wiigee/event/GestureEvent.java test.java test2.java org/wiigee/logic/TriggeredProcessingUnit.java org/wiigee/filter/*.java

clean:
	rm -f org/wiigee/event/*.class org/wiigee/control/*.class org/wiigee/device/*.class org/wiigee/filter/*.class org/wiigee/logic/*.class org/wiigee/util/*.class
