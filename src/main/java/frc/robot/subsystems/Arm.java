 package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Hardware.ArmHardware;

public class Arm extends SubsystemBase{
    private ArmHardware hardware;
    private final double ANGLE_MAX = 180;
    private final double ANGLE_MIN = 0;
    private static Arm instance;
    private double angle;
    
    private Arm(){
        hardware = Hardware.ArmHardware.getInstance();
    }

    public static Arm getInstance(){
        if(instance == null){
            instance = new Arm();
        }
        return instance;
    }


    public static enum ArmStates{
        Intake(0),
        Fender(0);
        public final double angle;
         ArmStates(double angle){
                this.angle = angle;
        }
    }

    public void accept(ArmStates armStates){
        accept(armStates.angle);
    }

    public void accept(double angle){
        // if(angle > ANGLE_MAX || angle < ANGLE_MIN){
        //     return;
        // }
        this.angle = angle;
    }

    @Override
    public void periodic() {
        hardware.execute(angle);
        // if(Math.random() > .9){
        //     System.out.println(angle);
        // }
    }

    
}