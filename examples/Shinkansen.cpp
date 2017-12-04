/**
 * @file Shinkansen.cpp
 * @brief Shinkansenクラスの関数を定義<br>
 * @author Futa HIRAKOBA
 */

#include "Shinkansen.h"
enum TrafficLight
{
    RED,
    YELLOG,
    GREEN
};

struct Person {
    char name[20];
     int sex;
     int age;
  double height;
  double weight;
};

struct Person p;

void Shinkansen::colorDetection(){
	colorid_t circleColor, blockColor;
	int8_t count = 0;
	//たのうえ
	while(1){
		circleColor = colorSensor.getColorNumber();
		if(circleColor == COLOR_BLUE || circleColor == COLOR_GREEN || circleColor == COLOR_YELLOW || circleColor == COLOR_RED){
			ev3_speaker_play_tone (NOTE_FS6, 100);
			basicWalker.backStraight(50);
			lifter.changeDefault(60);
			blockColor = colorSensor.getColorNumber();
			while(blockColor != COLOR_BLUE && blockColor != COLOR_GREEN && blockColor != COLOR_YELLOW && blockColor != COLOR_RED){
				if(count > 10)break;
				basicWalker.goStraight(10, 10);
        		blockColor = colorSensor.getColorNumber();
				count++;
        	}
			lifter.changeDefault(-60);
			if(circleColor == blockColor){
				basicWalker.goStraight(10, 50);
				ev3_speaker_play_tone (NOTE_FS6, 100);
			}else{
				basicWalker.goStraight(10, 230 - 10 * count);
				basicWalker.backStraight(100);
                // 古いバージョン
				// basicWalker.backStraight(10, 100);
				ev3_speaker_play_tone (NOTE_FS6, 200);
				
			}
			lifter.liftDown();
    		lifter.changeDefault(0);
			break;
		}
	}
}

