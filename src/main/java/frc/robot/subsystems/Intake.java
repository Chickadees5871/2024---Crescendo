package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hardware.IntakeHardware;

public class Intake extends SubsystemBase {
    private IntakeHardware intakeHardware;
    private static Intake instance;

    private Intake(){
        intakeHardware = Hardware.IntakeHardware.getInstance();
    }
    public static Intake getInstance(){
        if(instance == null){
            instance = new Intake();
        }
        return instance;
    }

    public void accept(double dutyCycle){ //the velocity value
        intakeHardware.execute(dutyCycle);
    }
}
