package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;

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
}
