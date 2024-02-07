package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hardware.ShooterHardware;

public class Shooter extends SubsystemBase {
    private ShooterHardware shooterHardware;
    private static Shooter instance;

    public Shooter(){
        shooterHardware = Hardware.ShooterHardware.getInstance();
    }
    public static Shooter getInstance(){
        if(instance == null){
            instance = new Shooter();
        }
        return instance;
    }

    public void accept(double voltage){
        shooterHardware.execute(voltage);
    }
}
