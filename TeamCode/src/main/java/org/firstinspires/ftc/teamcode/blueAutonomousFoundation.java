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
@Autonomous(name="Blue Right - Move Foundation and Park")
public class blueAutonomousFoundation extends LinearOpMode {

    /* Declare OpMode members. */
    greaseBBQLightning      robot   = new greaseBBQLightning();   // Use a Grease BBQ Lighting Hardware Setup
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = .5;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();
        try {
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e){
            telemetry.addData("Status", "Could not reset");    //
            telemetry.addData("Error", e.getMessage());
            telemetry.update();
        }

        try{
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e){
            telemetry.addData("Status", "Could not run using encoder");    //
            telemetry.addData("Error", e.getMessage());
            telemetry.update();
        }


        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d :%7d :%7d",
                                  robot.myBigMotorFrontRight.getCurrentPosition(),
                                  robot.myBigMotorFrontLeft.getCurrentPosition(),
                                  robot.myBigMotorBackLeft.getCurrentPosition(),
                                  robot.myBigMotorBackRight.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.myBigServoFoundation.setPower(-1);

        driveToFoundation(1);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double fleftInches, double frightInches,
                             double bleftInches, double brightInches) {
        int newfLeftTarget;
        int newfRightTarget;
        int newbLeftTarget;
        int newbRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newfLeftTarget = robot.myBigMotorFrontLeft.getCurrentPosition() + (int)(-fleftInches * COUNTS_PER_INCH);
            newfRightTarget = robot.myBigMotorFrontRight.getCurrentPosition() + (int)(-frightInches * COUNTS_PER_INCH);
            newbLeftTarget = robot.myBigMotorFrontLeft.getCurrentPosition() + (int)(-bleftInches * COUNTS_PER_INCH);
            newbRightTarget = robot.myBigMotorFrontRight.getCurrentPosition() + (int)(-brightInches * COUNTS_PER_INCH);
            robot.myBigMotorFrontLeft.setTargetPosition(newfLeftTarget);
            robot.myBigMotorFrontRight.setTargetPosition(newfRightTarget);
            robot.myBigMotorBackLeft.setTargetPosition(newbLeftTarget);
            robot.myBigMotorBackRight.setTargetPosition(newbRightTarget);

            // Turn On RUN_TO_POSITION
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.myBigMotorFrontLeft.setPower(Math.abs(speed));
            robot.myBigMotorFrontRight.setPower(Math.abs(speed));
            robot.myBigMotorBackLeft.setPower(Math.abs(speed));
            robot.myBigMotorBackRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while (opModeIsActive()
                    //&&
                   //(runtime.seconds() < timeoutS)
                    &&
                    (robot.myBigMotorFrontLeft.isBusy() && robot.myBigMotorFrontRight.isBusy()
                            && robot.myBigMotorBackLeft.isBusy() && robot.myBigMotorBackRight.isBusy())
            )

            {

                // Display it for the driver.
                //telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                                            robot.myBigMotorFrontLeft.getCurrentPosition(),
                                            robot.myBigMotorFrontRight.getCurrentPosition(),
                                            robot.myBigMotorBackLeft.getCurrentPosition(),
                                            robot.myBigMotorBackRight.getCurrentPosition());
                telemetry.update();
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

            //  sleep(250);   // optional pause after each move
        }
    }

    public void driveToFoundation(double speed){
        //Raise RandP
        robot.setRackAndPinionHeight(1440,1);
        //Move off of wall
        encoderDrive(.5,4,4,4,4);
        //Strafing left to center to foundation
        encoderDrive(.5,-6,6,6,-6);
        //Move to foundation
        encoderDrive(.5,25,25,25,25);
        //Drop RandP onto Foundation
        robot.setRackAndPinionHeight(0,1);
        //Wait for RandP to land on foundation
        sleep(1000);
        //Pull foundation backwards to wall
        encoderDrive(.5,-28,-28,-28,-28);
        //Lift RandP off of foundation before strafing to park
        robot.setRackAndPinionHeight(1440,1);
        //Wait for RandP to clear foundation before strafing
        sleep(1000);
        //Start strafing toward parking zone
        encoderDrive(1,20,-20,-20,20);
        //Set RandP back down to start position
        robot.setRackAndPinionHeight(0,1);
        sleep(1000);
        //Finish strafing to parking zone
        encoderDrive(1,23,-23,-23,23);
    }
}
