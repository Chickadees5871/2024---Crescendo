package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Hardware {
    // define subsystem hardware here. Swerve is self contained so it is ignored
    public static class ArmHardware {
        private CANSparkMax leader;
        private CANSparkMax follower;
        private ProfiledPIDController pidController;
        private RelativeEncoder encoder;
        // private SparkPIDController leadController;
        private static final double kF = .1;
        private static ArmHardware instance;
        
        public static synchronized ArmHardware getInstance(){
            if(instance == null){
                instance = new ArmHardware();
            }
            return instance;
        }

        private ArmHardware() {
            leader = new CANSparkMax(50, MotorType.kBrushless); // need to be filled
            follower = new CANSparkMax(51, MotorType.kBrushless); // need to be filled
            follower.follow(leader);
            encoder = leader.getEncoder();
            encoder.setPositionConversionFactor(360); //must be filled empirically
            // needs to be tuned empirically
            pidController = new ProfiledPIDController(.1, 0, 0,
                    new Constraints(1, 1));

        }

        private static double calculateFF(double theta) {
            return kF * Math.cos(theta);
        }
        // Takes in target angle, and drives towards target angle;
        public synchronized void execute(double position) {
            var output = pidController.calculate(encoder.getPosition(), position);
            output += calculateFF(position);
            leader.setVoltage(output);
        }
    }

    public static class ShooterHardware{
        private CANSparkMax shooterMotor;
        private static ShooterHardware instance;
        private ShooterHardware(){
            shooterMotor = new CANSparkMax (61,MotorType.kBrushed);
        }

        public static ShooterHardware getInstance(){
            if(instance == null){
                instance = new ShooterHardware();
            }
            return instance;
        }

        public void execute(double voltage){
            shooterMotor.setVoltage(voltage);
        }
    }

    public static class IntakeHardware{
        private CANSparkMax intakeMotor;
        private static IntakeHardware instance;
        private IntakeHardware(){
            intakeMotor = new CANSparkMax (60,MotorType.kBrushed);
        }

        public static IntakeHardware getInstance(){
            if(instance == null){
                instance = new IntakeHardware();
            }
            return instance;
        }

        public void execute(double voltage){
            intakeMotor.setVoltage(voltage);
        }
    }
}
