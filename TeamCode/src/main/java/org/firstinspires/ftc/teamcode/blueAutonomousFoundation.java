/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 * *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 */
//@Disabled
@Autonomous(name="Autonomous")
public class blueAutonomousFoundation extends LinearOpMode {

    /* Declare OpMode members. */
    greaseBBQLightning      robot   = new greaseBBQLightning();   // Use a Grease BBQ Lighting Hardware Setup
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double     RED_LOWER_BOUND         = 9;
    static final double     RED_UPPER_BOUND         = 16;
    static final double     BLUE_LOWER_BOUND        = 209;
    static final double     BLUE_UPPER_BOUND        = 240;

    String myBigColor = null;

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    //final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


//        robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.myBigMotorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        robot.myBigMotorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        robot.myBigMotorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        robot.myBigMotorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        robot.myBigMotorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        robot.myBigMotorRandP.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Initialized Positions",  "Running at %7d :%7d :%7d :%7d :%7d",
                robot.myBigMotorFrontLeft.getCurrentPosition(),
                robot.myBigMotorFrontRight.getCurrentPosition(),
                robot.myBigMotorBackLeft.getCurrentPosition(),
                robot.myBigMotorBackRight.getCurrentPosition(),
                robot.myBigMotorRandP.getCurrentPosition());
        telemetry.update();



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while(opModeIsActive() && runtime.seconds() < 30) {

            driveToFoundation(1);
            break;
        }
    }


    public void encoderDrive(double speed, double fleftInches, double frightInches, double bleftInches, double brightInches)
    {
        int newfLeftTarget;
        int newfRightTarget;
        int newbLeftTarget;
        int newbRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newfLeftTarget = robot.myBigMotorFrontLeft.getCurrentPosition() + (int)(-fleftInches * COUNTS_PER_INCH);
            newfRightTarget = robot.myBigMotorFrontRight.getCurrentPosition() + (int)(-frightInches * COUNTS_PER_INCH);
            newbLeftTarget = robot.myBigMotorBackLeft.getCurrentPosition() + (int)(-bleftInches * COUNTS_PER_INCH);
            newbRightTarget = robot.myBigMotorBackRight.getCurrentPosition() + (int)(-brightInches * COUNTS_PER_INCH);
            robot.myBigMotorFrontLeft.setTargetPosition(newfLeftTarget);
            robot.myBigMotorFrontRight.setTargetPosition(newfRightTarget);
            robot.myBigMotorBackLeft.setTargetPosition(newbLeftTarget);
            robot.myBigMotorBackRight.setTargetPosition(newbRightTarget);

            // Turn On RUN_TO_POSITION
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            robot.myBigMotorFrontLeft.setPower(Math.abs(speed));
            robot.myBigMotorFrontRight.setPower(Math.abs(speed));
            robot.myBigMotorBackLeft.setPower(Math.abs(speed));
            robot.myBigMotorBackRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and all motors running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means if all motors are not
            // running to position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that ANY motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while (opModeIsActive()
                    &&
                    (robot.myBigMotorFrontLeft.isBusy() && robot.myBigMotorFrontRight.isBusy()
                            && robot.myBigMotorBackLeft.isBusy() && robot.myBigMotorBackRight.isBusy())
            )

            {

                // Display it for the driver.
                //telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
//                telemetry.addData("Current Path",  "Running at %7d :%7d :%7d :%7d",
//                                            robot.myBigMotorFrontLeft.getCurrentPosition(),
//                                            robot.myBigMotorFrontRight.getCurrentPosition(),
//                                            robot.myBigMotorBackLeft.getCurrentPosition(),
//                                            robot.myBigMotorBackRight.getCurrentPosition());
//                telemetry.update();
            }

            // Stop all motion;
            robot.myBigMotorFrontLeft.setPower(0);
            robot.myBigMotorFrontRight.setPower(0);
            robot.myBigMotorBackLeft.setPower(0);
            robot.myBigMotorBackRight.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void driveToFoundation(double speed){
        robot.myBigServoFoundation.setPower(-1);
        robot.myBigServoClaw.setPower(-.1);
        robot.setRackAndPinionHeight(500,1);
        //Move to foundation
        encoderDrive(speed,15,15,15,15);

        //Slow down to detect color of foundation
        robot.myBigMotorFrontLeft.setPower(-.25);
        robot.myBigMotorFrontRight.setPower(-.25);
        robot.myBigMotorBackLeft.setPower(-.25);
        robot.myBigMotorBackRight.setPower(-.25);

        //While moving forward, continuously check the color sensor for red or blue.
        //These values will have to be calibrated before each tournament since the values
        //can fluctuate depending on lighting conditions. The STATIC variables for red and blue
        //are defined above and should be changed based on calibrations
        while(opModeIsActive()){

            Color.RGBToHSV((int) (robot.myBigColorSensor.red() * SCALE_FACTOR),
                    (int) (robot.myBigColorSensor.green() * SCALE_FACTOR),
                    (int) (robot.myBigColorSensor.blue() * SCALE_FACTOR),
                    hsvValues);

            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();

            if(hsvValues[0] > RED_LOWER_BOUND && hsvValues[0] < RED_UPPER_BOUND){
                myBigColor = "RED";
                break;
            } else if (hsvValues[0] > BLUE_LOWER_BOUND && hsvValues[0] < BLUE_UPPER_BOUND){
                myBigColor = "BLUE";
                break;
            }
        }

        telemetry.addData("Color", myBigColor);
        telemetry.update();

        //Strafing to center to foundation depending on color detected
        if(myBigColor == "BLUE"){
            encoderDrive(.25,3,3,3,3);
            encoderDrive(1,-6,6,6,-6);
        }

        else if(myBigColor == "RED"){
            encoderDrive(.25,3,3,3,3);
            encoderDrive(speed,6,-6,-6,6);
        }

        sleep(200);
        //Drop Foundation Mover
        robot.myBigServoFoundation.setPower(0);
        robot.setRackAndPinionHeight(0,1);

        //Pull foundation backwards to wall slow then fast to allow claw grasp
        encoderDrive(.25,-20,-20,-20,-20);
        //encoderDrive(1,-10,-10,-10,-10);

        //Rotate Foundation
//        if(myBigColor == "BLUE"){
//            encoderDrive(1,4,-4,-4,4);
//            encoderDrive(.25,-10,10,-10,10);
//            encoderDrive(1,4,-4,-4,4);
//            encoderDrive(.25,-10,10,-10,10);
//        }

//        else if(myBigColor == "RED"){
//            encoderDrive(1,-4,4,4,-4);
//            encoderDrive(.25,10,-10,10,-10);
//            encoderDrive(1,-4,4,4,-4);
//            encoderDrive(.25,10,-10,10,-10);
//        }

        //Raise Foundation Mover
        robot.myBigServoFoundation.setPower(-1);
        
        //Raise and lower RnP to free up claw
        robot.setRackAndPinionHeight(150,1);

        //Rotate towards bridge (color dependent)
//        if(myBigColor == "BLUE"){
//            encoderDrive(speed,10,-10,10,-10);
//        }
//
//        else if(myBigColor == "RED"){
//            encoderDrive(speed,-10,10,-10,10);
//        }
        
        //Strafe towards bridge to park or continue and lower RnP
        robot.setRackAndPinionHeight(0,1);

        if(myBigColor == "BLUE"){
            encoderDrive(speed,29,-29,-29,29);
        }

        else if(myBigColor == "RED"){
            encoderDrive(speed,-29,29,29,-29);
        }




    }
}
