package it.unito.di.robot;

public class RobotException extends Throwable {
        public RobotException(String message) {
            super(message);
        }

    @Override
    public String toString() {
        return "[RobotException] - " + getMessage();
    }

}
