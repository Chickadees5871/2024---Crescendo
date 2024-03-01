package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Hardware;

public class ArmGoToCommand extends Command {
    private Arm arm;
    private double setpoint;
    public ArmGoToCommand(Arm arm, double value){
        this.arm = arm;
        addRequirements(arm);
        this.setpoint = value;
    
    }

    @Override
    public void initialize() {
        arm.accept(setpoint);
        System.out.println("Hi I POTATO");
    }

    @Override
    public boolean isFinished() {
        return Math.abs(setpoint - Hardware.ArmHardware.getInstance().getEncoder().getPosition()) < 2;
    }
}
