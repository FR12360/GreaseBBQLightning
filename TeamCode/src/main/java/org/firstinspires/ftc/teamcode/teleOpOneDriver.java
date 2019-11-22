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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a four wheeled robot
 *
 */

@TeleOp(name="Driver Mode")

public class Driver extends LinearOpMode {

    // Declare OpMode members.
    private greaseBBQLightning robot = new greaseBBQLightning();
    private ElapsedTime runtime = new ElapsedTime();
    private double leftClawPower = 0;
    private double rightClawPower = 0;
    private double rearClawPower = -.7;
    private boolean foundationClawUp = false;
    private boolean stoneGrabbed = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        robot.init(hardwareMap);

        //Set claw starting position
        robot.myBigServoLeftClaw.setPower(leftClawPower);
        robot.myBigServoRightClaw.setPower(rightClawPower);
        robot.myBigServoFoundationMover.setPower(rearClawPower);

        //robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            //Added conditional statement to turn on axis
            //when there is no acceleration (drive)
            /*if(drive == 0 && turn > 0){
                leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            } else if(drive == 0 && turn < 0) {
                leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            } else {
                leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            }*/

            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;


            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            //We reversed the front of the robot so these variables will need to change to
            //Correctly represent orientation
            robot.myBigMotorFrontLeft.setPower(rightPower);
            robot.myBigMotorBackLeft.setPower(rightPower);
            robot.myBigMotorFrontRight.setPower(leftPower);
            robot.myBigMotorBackRight.setPower(leftPower);

            //Open claw
            if(this.gamepad1.left_bumper){
                runtime.reset();
                /*robot.myBigMotorRandP.setPower(.2);
                while(runtime.seconds() < .5) {
                    telemetry.addData("RandP Runtime", "Run Time: " + runtime.toString());
                    telemetry.update();
                }
                robot.myBigMotorRandP.setPower(0);
                sleep(100);*/
                robot.myBigServoLeftClaw.setPower(0);
                robot.myBigServoRightClaw.setPower(0);
                stoneGrabbed = false;
            }

            //Grab block
            if(this.gamepad1.right_bumper){
                runtime.reset();
                robot.myBigServoLeftClaw.setPower(-1);
                robot.myBigServoRightClaw.setPower(1);
                /*sleep(100);
                robot.myBigMotorRandP.setPower(-.3);
                while(runtime.seconds() < .5) {
                    telemetry.addData("RandP Runtime", "Run Time: " + runtime.toString());
                    telemetry.update();
                }
                robot.myBigMotorRandP.setPower(0);*/
                stoneGrabbed = true;
            }

            //lift claw
            if(this.gamepad1.right_trigger > 0){


                while(this.gamepad1.right_trigger > 0) {
                    robot.myBigMotorRandP.setPower(-.3);
                    robot.rAndPHeight += 1;
                }
                robot.myBigMotorRandP.setPower(0);
            }

            //lower claw
            if(this.gamepad1.left_trigger > 0){


                while(this.gamepad1.left_trigger > 0) {
                    robot.myBigMotorRandP.setPower(.3);
                    robot.rAndPHeight -= 1;
                }
                robot.myBigMotorRandP.setPower(0);
            }


            //Drop foundation claw

            if(this.gamepad1.b){

               if(!foundationClawUp){
                    robot.myBigServoFoundationMover.setPower(1);
                    foundationClawUp = true;
                }

            }

            //Raise foundation claw
            if(this.gamepad1.y){

                if(foundationClawUp){
                    robot.myBigServoFoundationMover.setPower(rearClawPower);
                    foundationClawUp = false;
                }

            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("RandP Height", "Height Counter: " + robot.rAndPHeight);
            telemetry.update();
            //telemetry.addData("Left Trigger", this.gamepad1.left_trigger);
            //telemetry.addData("Left Trigger", this.gamepad1.right_trigger);
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            //telemetry.addData("Rack and Pinion Motor",  "Starting at %7d", robot.myBigMotorFrontRight.getCurrentPosition());
            //telemetry.addData("Front Left Motor Power", myBigMotorFrontLeft.getPower());
            //telemetry.addData("Front Right Motor Power", myBigMotorFrontRight.getPower());
            //telemetry.addData("Back Left Motor Power", myBigMotorBackLeft.getPower());
            //telemetry.addData("Back Right Motor Power", myBigMotorBackRight.getPower());
            //telemetry.addData("Direction", myBigServoArmBase.getDirection());
            //telemetry.addData("Arm Base", robot.myBigServoArmBase.getPower());
            //telemetry.addData("Direction", myBigServoArmMid.getDirection());
            ///telemetry.addData("Arm Mid", robot.myBigServoArmMid.getPower());
            //telemetry.addData("Direction", myBigServoArmMid.getDirection());
            //telemetry.addData("Arm Claw", robot.myBigServoClaw.getPower());
            telemetry.update();
        }
    }
}
