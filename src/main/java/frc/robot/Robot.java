// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.commands.ArmGoToCommand;
<<<<<<< HEAD
import frc.robot.subsystems.Arm;
=======
>>>>>>> 1a93957970ce2d6d09298726d1ddb3ddc2008797

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer robotContainer;

  private DigitalInput limitSwitch;

  @Override
  public void robotInit() {
    robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}
 
  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    robotContainer.driveCommand.schedule();

<<<<<<< HEAD
    new ArmGoToCommand(robotContainer.arm, 3).schedule();
    System.out.println("Here");

    // limitSwitch = new DigitalInput(0);
=======
    new ArmGoToCommand(robotContainer.arm, 2);

    limitSwitch = new DigitalInput(0);
>>>>>>> 1a93957970ce2d6d09298726d1ddb3ddc2008797
  }

  @Override
  public void teleopPeriodic() {   
<<<<<<< HEAD
    // if(limitSwitch.get() == true && robotContainer.shooter.getActive() == false) {
    //   robotContainer.intake.accept(0);
    // }
=======
    if(limitSwitch.get() == true && robotContainer.shooter.getActive() == false) {
      robotContainer.intake.accept(0);
    }
>>>>>>> 1a93957970ce2d6d09298726d1ddb3ddc2008797
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
