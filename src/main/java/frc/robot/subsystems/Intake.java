package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hardware.IntakeHardware;

public class Intake extends SubsystemBase {
    private IntakeHardware IntakeHardware;
    private static Intake instance;

    public Intake(){
        IntakeHardware = Hardware.IntakeHardware.getInstance();
    }
    public static Intake getInstance(){
        if(instance == null){
            instance = new Intake();
        }
        return instance;
    }

    public void accept(double voltage){
        IntakeHardware.execute(voltage);
    }
}
