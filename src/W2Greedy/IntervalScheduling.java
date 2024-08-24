package W2Greedy;

import java.util.Scanner;

public class IntervalScheduling {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int arraySize = s.nextInt();

        //Vi indsætter alle vores scheduleItems i et array i O(n) tid.
        ScheduleItem[] scheduleItems = new ScheduleItem[arraySize];
        for (int i = 0; i < arraySize; i++) {
            int start = s.nextInt();
            int end = s.nextInt();
            scheduleItems[i] = new ScheduleItem(start, end);
        }

        //Sorter vores scheduleItems baseret på end tid i O(n logn).
        java.util.Arrays.sort(scheduleItems);

        int maxNonOverlappingTasks = 0;
        int lastEndTime = 0;
        //Vi løber igennem sorteret array i  O(n) tid.
        for (int i = 0; i < arraySize; i++) {
            //Hvis vi er færdige med tidligere event, så kan vi tage den næste event.
            if (scheduleItems[i].startTime >= lastEndTime) {
                //Vi tager den næste event.
                maxNonOverlappingTasks++;
                lastEndTime = scheduleItems[i].endTime;
            }
        }

        //Total runtime er O(n) + O(n logn) + O(n) = O(n logn)
        System.out.println(maxNonOverlappingTasks);

    }
}

class ScheduleItem implements Comparable<ScheduleItem> {
    //Vi har en start og sluttid på hver event.
    int startTime;
    int endTime;

    public ScheduleItem(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //Vi sortere baseret på end tid.
    @Override
    public int compareTo(ScheduleItem otherScheduleItem) {
        return this.endTime - otherScheduleItem.endTime;
    }
}



/*
* //Var til eksamen i den her.
//Logikken er at vi sortere alle tider
//Og så altid tager den tid som SLUTTER først.
//Lige så snart vi er done med den tid, så tager vi den næste tid som SLUTTER først.
//derfor er det en greedy algoritme, samt at runtime er O(n logn) pga. sorteringen.

//1 sorter alle tider baseret på slut tid Ascending

//2 Iterate through the sorted task list. For each task:
//•	If the current task can be scheduled without any conflicts (i.e., its start time is after the finish time of the previously scheduled task), add it to the schedule.
//•	Update the finish time of the last scheduled task to the finish time of the current task.

//3 Return final schedule

/*
*
3
1 4
2 8
5 9
*/
