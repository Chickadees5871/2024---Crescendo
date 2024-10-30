package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Hardware;

public class ArmGoToCommand extends Command {
    private Arm arm;
    private double setpoint;
    public ArmGoToCommand(Arm arm, double angle){
        this.arm = arm;
        addRequirements(arm);
        this.setpoint = angle;
    
    }

    @Override
    public void initialize() {
        arm.accept(setpoint);
    }

    @Override
    public boolean isFinished() {
        var calcPosition = Math.abs(setpoint - Hardware.ArmHardware.getInstance().getEncoder().getPosition());
        System.out.println("Calc arm position: " + calcPosition);
        return calcPosition < 3;
    }
}
