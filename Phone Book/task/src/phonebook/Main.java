package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    static long startTime;
    static long finishTime;
    public static void main(String[] args) {


        PhoneBook.addContactsFromTxt("/Users/yoelufland/Documents/PhoneBook/directory.txt");//filling List of contacts
        PhoneBook.readSearchedValues("/Users/yoelufland/Documents/PhoneBook/find.txt");//filling List of searching names
        linearSearch();                                     //calling linear search and calculating execution duration
        bubbleSortAndJumpOrLinearSearch();                  //calling bubble sort and linear search and calculating execution duration
        quickSortAndBinarySearch();                         //calling Quicksort and binary search and calculating execution duration
        hashTable();                                        //calling hashtable and calculating execution duration

    }

    static void linearSearch () {
        System.out.println("Start searching (linear search)...");
        startTime = System.currentTimeMillis();
        PhoneBook.contactsLinearSearch();                   //searching for values from listOfSearchingValues in contactsList
        finishTime = System.currentTimeMillis() - startTime;
        System.out.println("Time taken: "+ (finishTime / 1000 / 60) + " min. " + (finishTime / 1000 % 60) + " sec. "+(finishTime % 1000)+" ms.\n");
    }
    static void bubbleSortAndJumpOrLinearSearch() {
        System.out.println("Start searching (bubble sort + jump search)...");
        String linearSearchLog = "";
        String jumpSearchLog = "";
        startTime = System.currentTimeMillis();
        String SortingLog = PhoneBook.bubbleSort(finishTime);      //sorting contactsList, if sorting takes more than linear search * 10, stopping and using linear search
        if(SortingLog.contains("STOPPED")) {  //if Sorting was stopped
            linearSearchLog = PhoneBook.contactsLinearSearch();
        } else {
            jumpSearchLog = PhoneBook.contactsJumpSearch(); // if sorting is completed, using jump search to find values
        }
        finishTime = System.currentTimeMillis() - startTime;
        System.out.println("Time taken: "+ (finishTime / 1000 / 60) + " min. " + (finishTime / 1000 % 60) + " sec. "+(finishTime % 1000)+" ms.");
        System.out.println(SortingLog);
        if (!linearSearchLog.equals("")) {
            System.out.println(linearSearchLog);
        }
        if (!jumpSearchLog.equals("")) {
            System.out.println(jumpSearchLog);
        }
        System.out.println();
    }
    static void quickSortAndBinarySearch(){
        System.out.println("Start searching (quick sort + binary search)...");
        startTime = System.currentTimeMillis();

        String quickSortLog = QuickSort.quickSortHead(PhoneBook.listOfContacts,0,PhoneBook.listOfContacts.size()-1); //sorting using quicksort
        String binarySearchLog = BinarySearch.binarySearchHead();       //searching using binary search

        finishTime = System.currentTimeMillis() - startTime;

        System.out.println("Time taken: "+ (finishTime / 1000 / 60) + " min. " + (finishTime / 1000 % 60) + " sec. "+(finishTime % 1000)+" ms.");
        System.out.println(quickSortLog);
        System.out.println(binarySearchLog);
        System.out.println();
    }
    static void hashTable () {
        System.out.println("Start searching (hash table)...");
        startTime = System.currentTimeMillis();
        String hashMapCreatingLog = PhoneBook.addContactsFromTxtToMap("/Users/yoelufland/Documents/PhoneBook/directory.txt"); //adding contacts to HashMap As <Name, Number>
        String contactHashSearchLog = PhoneBook.contactsHashSearch();       //Searching values in map.
        finishTime = System.currentTimeMillis() - startTime;
        System.out.println("Time taken: "+ (finishTime / 1000 / 60) + " min. " + (finishTime / 1000 % 60) + " sec. "+(finishTime % 1000)+" ms.");
        System.out.println(hashMapCreatingLog);
        System.out.println(contactHashSearchLog);
    }
}


class PhoneBook {

    static Map<String, Integer> ContactMap = new HashMap<>(5_000_000);    //stores <String - Name, Integer - Number>

    static List<Contact> listOfContacts = new ArrayList<>();        //stores Contact instances
    static List<String> listOfSearchedValues = new ArrayList<>();   //stores <String> contact names

    public static void addContactsFromTxt(String path) {                     //putting values from .txt file to setOfContacts
        File contactsFile = new File(path);
        try {
            Scanner sc = new Scanner(contactsFile);
            while (sc.hasNext()) {
                int tempNumber = sc.nextInt();
                String tempName = sc.nextLine().trim();
                listOfContacts.add(new Contact(tempName, tempNumber));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }
    }

    public static String addContactsFromTxtToMap(String path) {               //putting values from .txt file to ContactMap

        long startTime = System.currentTimeMillis();
        File contactsFile = new File(path);
        try {
            Scanner sc = new Scanner(contactsFile);
            while (sc.hasNext()) {
                int tempNumber = sc.nextInt();
                String tempName = sc.nextLine().trim();
                ContactMap.put(tempName,tempNumber);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        }
        long endTime = System.currentTimeMillis() - startTime;
        return "Creating time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";
    }

    public static String contactsLinearSearch() {            //finding names from .txt files, counting them, and printing amount
        long startTime = System.currentTimeMillis();
        int foundValuesCount = 0;
        int searchedValuesCount = 0;
        for (String str : listOfSearchedValues) {
            for (Contact c : listOfContacts) {
                if (str.equals(c.name)) {
                    foundValuesCount++;
                }
            }
            searchedValuesCount++;
        }
        System.out.print("Found " + foundValuesCount + " / " + searchedValuesCount + " entries. ");
        long endTime = System.currentTimeMillis() - startTime;
        return "Searching time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";
    }

    public static void readSearchedValues(String path) {    //reading names from .txt file and adding them to the ListOfSearchedValues
        try {
            Scanner sc = new Scanner(new File(path));

            while (sc.hasNext()) {
                listOfSearchedValues.add(sc.nextLine().trim());
            }

        } catch (FileNotFoundException e) {
            System.out.println("FileNot found!");
        }
    }

    public static String bubbleSort(long timeLimit) { // Using bubble sort algorithm to sort ContactsList and return log message about execution duration
        timeLimit *= 10;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < listOfContacts.size(); i++) {

            long searchingTime = System.currentTimeMillis() - startTime;
            if(timeLimit < searchingTime) {     // if sorting time is longer than 10 times linear search time, stopping sorting and retuning sorting time.
               return "Sorting time: "+ (searchingTime / 1000 / 60) + " min. " + (searchingTime / 1000 % 60) + " sec. "+(searchingTime % 1000)+" ms. - STOPPED, moved to linear search";
            }

            for (int j = i + 1; j < listOfContacts.size(); j++) {
                if (listOfContacts.get(j).name.compareTo(listOfContacts.get(i).name) < 0) {
                    Contact temp = listOfContacts.get(i);
                    listOfContacts.set(i,listOfContacts.get(j));
                    listOfContacts.set(j,temp);
                }
            }
        }
        long searchingTime = System.currentTimeMillis() - startTime;
        return  "Sorting time: "+ (searchingTime / 1000 / 60) + " min. " + (searchingTime / 1000 % 60) + " sec. "+(searchingTime % 1000)+" ms.";

    }

    public static String contactsJumpSearch() {             //Using jump search algorithm to  find values returns log message about execution duration
        long startTime = System.currentTimeMillis();
        int foundValuesCount = 0;
        int searchedValuesCount = 0;
        for (String str : listOfSearchedValues) {
            searchedValuesCount++;
            int step = (int) Math.sqrt(listOfContacts.size());
            int start = 0;
            int end = step;

            while (str.compareTo(listOfContacts.get(end).name) > 0) {//jumps through values until finds value bigger or equal to searched one
                start = end;
                if(end + step <= listOfContacts.size()){
                    end = listOfContacts.size() - 1;
                } else {
                    end += step;
                }
            }

            for (int i = end; i >= start; i--){//reversed linear search in block of values
                if (listOfContacts.get(i).name.equals(str)) {
                    foundValuesCount++;
                }
            }
        }
        System.out.print("Found " + foundValuesCount + " / " + searchedValuesCount + " entries. ");
        long endTime = System.currentTimeMillis() - startTime;
        return "Searching time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";
    }

    public static String contactsHashSearch() {     //searching values from ListOfSearchedValues in ContactMap
        long startTime = System.currentTimeMillis();
        int foundValuesCount = 0;
        int searchedValuesCount = 0;
        for (String str : listOfSearchedValues) {
            searchedValuesCount++;
            if (ContactMap.get(str) != null) {
                foundValuesCount++;
            }
        }
        System.out.print("Found " + foundValuesCount + " / " + searchedValuesCount + " entries. ");
        long endTime = System.currentTimeMillis() - startTime;
        return "Searching time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";
    }

}
class Contact {                     //stores individual contact data
    String name;
    int number;

    public Contact(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}


class QuickSort { // Sorting List of Contacts

    static String quickSortHead(List<Contact> array, int low, int high){
        long startTime = System.currentTimeMillis();

        quickSort(array,low,high);
        long endTime = System.currentTimeMillis() - startTime;
        return "Sorting time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";

    }

    static void quickSort(List<Contact> array, int low, int high){
        if(low < high){                 //condition that stops recursion
            int pivotIndex = partition(array, low, high);       //method put last value from array to its correct place
            quickSort(array, low, pivotIndex -1);          //calling method recursively for left part of array(excluding index)
            quickSort(array, pivotIndex + 1, high);         //calling method recursively for right part of array(excluding index)
        }
    }

    static int partition(List<Contact> arr, int low, int high)
    {


        String pivot = arr.get(high).name;      //choosing last element as pivot



        int i = (low - 1);          // Index of smaller than pivot element

        for(int j = low; j <= high - 1; j++)
        {


            if (arr.get(j).name.compareTo(pivot) < 0)     //if value is smaller than pivot, swapping it with next vale after the smallest one
            {


                i++;        //increments index of smaller than pivot element
                swap(arr, i, j);//swapping next value after the smallest one with j
            }
        }
        swap(arr, i + 1, high);//swap next element after the smallest one with our index(that operation puts our pivot value in place)
        return (i + 1);//returns index of pivot
    }


    static void swap (List<Contact> arr, int i, int y) {
        Contact temp = arr.get(i);
        arr.set(i,arr.get(y));
        arr.set(y,temp);
    }
}

class BinarySearch {   //Searching for contacts using binary search algorithm

    static String binarySearchHead (){
        long startTime = System.currentTimeMillis();
        int foundValuesCount = 0;
        int searchedValuesCount = 0;
        for (String str : PhoneBook.listOfSearchedValues) {

            foundValuesCount += binarySearch(PhoneBook.listOfContacts, str);
            searchedValuesCount++;
        }
        System.out.print("Found " + foundValuesCount + " / " + searchedValuesCount + " entries. ");
        long endTime = System.currentTimeMillis() - startTime;
        return "Searching time: "+ (endTime / 1000 / 60) + " min. " + (endTime / 1000 % 60) + " sec. "+(endTime % 1000)+" ms.";
    }


    static int binarySearch(List<Contact> arr, String value)
    {
        int start = 0;
        int end = arr.size() - 1;
        while (start <= end) {
            int middle = start + (end - start) / 2;

            int res = value.compareTo(arr.get(middle).name);

            // Check if value is present at mid
            if (res == 0)
                return 1;

            // If value greater, ignore left half
            if (res > 0)
                start = middle + 1;

                // If value is smaller, ignore right half
            else
                end = middle - 1;
        }

        return -1;
    }
}
