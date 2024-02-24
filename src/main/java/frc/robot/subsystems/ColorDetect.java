// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import java.io.Console;
import com.revrobotics.ColorMatch;

public class ColorDetect extends SubsystemBase{

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final Color kRedTarget = new Color(0.561, 0.232, 0.114);

  public void matching_Color() {

    m_colorMatcher.addColorMatch(kRedTarget);

  }

  public void set_Color() {

    Color detectedColor = m_colorSensor.getColor();

    String colorString;
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kRedTarget) {
      colorString = "Red";
    } else {
      colorString = "Unknown";
    }

    System.out.println("Red-" + detectedColor.red);
    System.out.println("Confidence-" + match.confidence);
    System.out.println("Detected Color-" + colorString);
  }

}