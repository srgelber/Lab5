import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

//Simon Gelber and Kush Upadhyay: CPE315(SENG), Section 5
public class lab3 {

    public static void main(String[] args)throws FileNotFoundException{

        Map<String, Integer> registers  = new HashMap<String, Integer>() {{
            put("pc", 0);
            put("zero", 0);
            put("0", 0);
            put("v0", 0);
            put("v1", 0);
            put("a0", 0);
            put("a1", 0);
            put("a2", 0);
            put("a3", 0);
            put("t0", 0);
            put("t1", 0);
            put("t2", 0);
            put("t3", 0);
            put("t4", 0);
            put("t5", 0);
            put("t6", 0);
            put("t7", 0);
            put("s0", 0);
            put("s1", 0);
            put("s2", 0);
            put("s3", 0);
            put("s4", 0);
            put("s5", 0);
            put("s6", 0);
            put("s7", 0);
            put("t8", 0);
            put("t9", 0);
            put("sp", 0);
            put("ra", 0);
        }};

        Dictionary labelDict = new Hashtable();
        Scanner scanner = new Scanner(new File(args[0]));
        int count = 0;

        //first pass to get label addresses
        while(scanner.hasNextLine()){
            boolean flag = false;
            for (String x : splitLine(scanner.nextLine())){
                if (x.contains(":")){
                    labelDict.put(x.substring(0,x.length()-1), count);
                }
                if(isInstruction(x)){
                    flag = true;
                }
            }
            if(flag == true)
                count += 1;
        }
        scanner.close();

        //second pass
        scanner = new Scanner(new File(args[0]));
        int lineCount = 0;
        ArrayList<ArrayList<String>> instructionArray = new ArrayList<ArrayList<String>>();
        while(scanner.hasNextLine()){
            String line0 = scanner.nextLine().replaceAll(":(?!$)", ": ");
            line0 = line0.replaceAll("#(?!$)", " #");
            ArrayList<String> lineFinal = getInstruction(line0, labelDict, lineCount);
            if(!lineFinal.isEmpty()){
                instructionArray.add(lineFinal);
            }
            //System.out.println(lineFinal);
            if(!lineFinal.equals("")){

                lineCount += 1;
            }
        }
        //System.out.print(instructionArray);

        scanner.close();

        // start of lab3
        int[] dataMem = new int[8192];
        Scanner myObj = null;
        boolean scriptFlag = false;

        if(args.length == 2){

            myObj = new Scanner(new File(args[1]));
            scriptFlag = true;
        }
        else{

            myObj = new Scanner(System.in);
        }
        String[] input = {"0"};
        int currPos = 0;    //pointer to current inst in instArray

        while (!input[0].equals("q")){
            System.out.print("mips> ");

            if(scriptFlag == true){
                if(myObj.hasNext()){
                    input = myObj.nextLine().trim().split(" ");
                }
                else{
                    System.exit(0);
                }
            }
            else{
                input = myObj.nextLine().trim().split(" ");
            }
            if(scriptFlag == true){
                if (input[0].equals("s") && input.length > 1){
                        System.out.println(input[0] + " " + input[1]);
                }
                else if (input[0].equals("m")){
                    System.out.println(input[0] + " " + input[1] + " " + input[2]);
                }
                else System.out.println(input[0]);
            }

            // interactive input
            if (input[0].equals("s")){
                 int iter = 1;
                 int cnt = 0;

                 if (input.length > 1){
                     int num = Integer.parseInt(input[1]);
                     if( num > 0){
                         iter = num;
                     }
                 }

                 while (cnt != iter && currPos < instructionArray.size()){
                     ArrayList currCmd = instructionArray.get(currPos);

                     //System.out.println(currCmd);

                     if (currCmd.get(0).equals("add")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String readReg2 = (String) currCmd.get(3);

                         int res = registers.get(readReg1) + registers.get(readReg2);

                         registers.put(destReg, res);
                     }
                     else if (currCmd.get(0).equals("and")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String readReg2 = (String) currCmd.get(3);

                         int res = registers.get(readReg1) & registers.get(readReg2);

                         registers.put(destReg, res);
                     }
                     else if (currCmd.get(0).equals("or")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String readReg2 = (String) currCmd.get(3);

                         int res = registers.get(readReg1) | registers.get(readReg2);

                         registers.put(destReg, res);
                     }
                     else if (currCmd.get(0).equals("sub")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String readReg2 = (String) currCmd.get(3);

                         int res = registers.get(readReg1) - registers.get(readReg2);

                         registers.put(destReg, res);
                     }
                     else if (currCmd.get(0).equals("slt")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String readReg2 = (String) currCmd.get(3);

                         if (registers.get(readReg1) < registers.get(readReg2)) registers.put(destReg, 1);
                         else registers.put(destReg, 0);
                     }
                     else if (currCmd.get(0).equals("sll")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String imm = (String) currCmd.get(3);

                         int res = registers.get(readReg1) << Integer.parseInt(imm);

                         registers.put(destReg, res);
                     }
                     else if (currCmd.get(0).equals("addi")){

                         String destReg = (String) currCmd.get(1);
                         String readReg1 = (String) currCmd.get(2);
                         String imm = (String) currCmd.get(3);

                         int res = registers.get(readReg1) + Integer.parseInt(imm);

                         registers.put(destReg, res);
                     }
                    else if (currCmd.get(0).equals("beq")){

                        String readReg1 = (String) currCmd.get(1);
                        String readReg2 = (String) currCmd.get(2);
                        String offset = (String) currCmd.get(3);

                        if (registers.get(readReg1) == registers.get(readReg2)) currPos = (int) labelDict.get(offset) - 1;

                    }
                    else if (currCmd.get(0).equals("bne")){

                        String readReg1 = (String) currCmd.get(1);
                        String readReg2 = (String) currCmd.get(2);
                        String offset = (String) currCmd.get(3);

                        if (registers.get(readReg1) != registers.get(readReg2)) currPos = (int) labelDict.get(offset) - 1;
                    }
                    else if (currCmd.get(0).equals("lw")){

                        String destReg = (String) currCmd.get(1);
                        String offset = (String) currCmd.get(2);
                        String srcReg = (String) currCmd.get(3);

                        int res = dataMem[registers.get(srcReg) + Integer.parseInt(offset)];

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("sw")){

                        String destReg = (String) currCmd.get(1);
                        String offset = (String) currCmd.get(2);
                        String srcReg = (String) currCmd.get(3);

                        dataMem[registers.get(srcReg) + Integer.parseInt(offset)] = registers.get(destReg);
                    }
                    else if (currCmd.get(0).equals("j")){

                        String target = (String) currCmd.get(1);

                        currPos = (int) labelDict.get(target) - 1;
                    }
                    else if (currCmd.get(0).equals("jal")){

                        String target = (String) currCmd.get(1);

                        registers.put("ra", currPos + 1);

                        currPos = (int) labelDict.get(target) - 1;
                    }
                    else if (currCmd.get(0).equals("jr")){
                        String readReg1 = (String) currCmd.get(1);

                        currPos = registers.get(readReg1) - 1;
                    }

                    currPos++;
                    registers.put("pc", currPos);

                    cnt++;
                 }

                System.out.println("\t" + iter + " instruction(s) executed");
            }
            else if (input[0].equals("r")) {

                while (currPos < instructionArray.size()){

                    ArrayList currCmd = instructionArray.get(currPos);
                    // System.out.println(currCmd);

                    if (currCmd.get(0).equals("add")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String readReg2 = (String) currCmd.get(3);

                        int res = registers.get(readReg1) + registers.get(readReg2);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("and")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String readReg2 = (String) currCmd.get(3);

                        int res = registers.get(readReg1) & registers.get(readReg2);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("or")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String readReg2 = (String) currCmd.get(3);

                        int res = registers.get(readReg1) | registers.get(readReg2);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("sub")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String readReg2 = (String) currCmd.get(3);

                        int res = registers.get(readReg1) - registers.get(readReg2);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("slt")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String readReg2 = (String) currCmd.get(3);

                        if (registers.get(readReg1) < registers.get(readReg2)) registers.put(destReg, 1);
                        else registers.put(destReg, 0);
                    }
                    else if (currCmd.get(0).equals("sll")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String imm = (String) currCmd.get(3);

                        int res = registers.get(readReg1) << Integer.parseInt(imm);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("addi")){

                        String destReg = (String) currCmd.get(1);
                        String readReg1 = (String) currCmd.get(2);
                        String imm = (String) currCmd.get(3);

                        int res = registers.get(readReg1) + Integer.parseInt(imm);

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("beq")){

                        String readReg1 = (String) currCmd.get(1);
                        String readReg2 = (String) currCmd.get(2);
                        String offset = (String) currCmd.get(3);

                        if (registers.get(readReg1) == registers.get(readReg2)) currPos = (int) labelDict.get(offset) - 1;

                    }
                    else if (currCmd.get(0).equals("bne")){

                        String readReg1 = (String) currCmd.get(1);
                        String readReg2 = (String) currCmd.get(2);
                        String offset = (String) currCmd.get(3);

                        if (registers.get(readReg1) != registers.get(readReg2)) currPos = (int) labelDict.get(offset) - 1;
                    }
                    else if (currCmd.get(0).equals("lw")){

                        String destReg = (String) currCmd.get(1);
                        String offset = (String) currCmd.get(2);
                        String srcReg = (String) currCmd.get(3);

                        int res = dataMem[registers.get(srcReg) + Integer.parseInt(offset)];

                        registers.put(destReg, res);
                    }
                    else if (currCmd.get(0).equals("sw")){

                        String destReg = (String) currCmd.get(1);
                        String offset = (String) currCmd.get(2);
                        String srcReg = (String) currCmd.get(3);

                        dataMem[registers.get(srcReg) + Integer.parseInt(offset)] = registers.get(destReg);
                    }
                    else if (currCmd.get(0).equals("j")){

                        String target = (String) currCmd.get(1);

                        currPos = (int) labelDict.get(target) - 1;
                    }
                    else if (currCmd.get(0).equals("jal")){

                        String target = (String) currCmd.get(1);

                        registers.put("ra", currPos + 1);

                        currPos = (int) labelDict.get(target) - 1;
                    }
                    else if (currCmd.get(0).equals("jr")){
                        String readReg1 = (String) currCmd.get(1);

                        currPos = registers.get(readReg1) - 1;
                    }

                    currPos++;
                    registers.put("pc", currPos);
                }

            }
            else if (input[0].equals("h")){
                System.out.println("\nh = show help\nd = dump register state\ns = single step through the program (i.e. execute 1 instruction and stop)\ns num = step through num instructions of the program\nr = run until the program ends\nm num1 num2 = display data memory from location num1 to num2\nc = clear all registers, memory, and the program counter to 0\nq = exit the program\n");

            }
            else if (input[0].equals("c")){
                Set regSet = registers.entrySet();
                Iterator it = regSet.iterator();
                while(it.hasNext()){
                    Map.Entry reg = (Map.Entry)it.next();
                    registers.replace((String)reg.getKey(),0);
                }
                System.out.println("Simulator reset\n");

            }
            else if (input[0].equals("m")){
                System.out.print("\n");
                for(int j = Integer.parseInt(input[1]);j<=Integer.parseInt(input[2]);j++){
                    System.out.println("["+j+"]" +" = " + dataMem[j]);
                }
                System.out.print("\n");

            }
            else if (input[0].equals("d")){
                System.out.println("\npc = "+registers.get("pc")+"\n$0 = "+registers.get("0")+"$v0 = "+registers.get("v0")+"$v1 = "+registers.get("v1")+"$a0 = "+registers.get("a0")+ "\n$a1 = "+registers.get("a1")+"$a2 = "+registers.get("a2")+"$a3 = "+registers.get("a3")+"$t0 = "+registers.get("t0")+"\n$t1 = "+registers.get("t1")+"$t2 = "+registers.get("t2")+"$t3 = "+registers.get("t3")+"$t4 = "+registers.get("t4")+"\n$t5 = "+registers.get("t5")+"$t6 = "+registers.get("t6")+"$t7 = "+registers.get("t7")+"$s0 = "+registers.get("s0")+"\n$s1 = "+registers.get("s1")+"$s2 = "+registers.get("s2")+"$s3 = "+registers.get("s3")+"$s4 = "+registers.get("s4")+"\n$s5 = "+registers.get("s5")+"$s6 = "+registers.get("s6")+"$s7 = "+registers.get("s7")+"$t8 = "+registers.get("t8")+"\n$t9 = "+registers.get("t9")+"$sp = "+registers.get("sp")+"$ra = "+registers.get("ra")+"\n");

            }

        }

    }
    //function which processes each line
    public static ArrayList<String> getInstruction(String line, Dictionary labelDict, int lineCount){
        short fixedImm = 0000000000000000;
        String output = "";
        ArrayList<String> outputList = new ArrayList<String>();

        try{
            if(isBlankLine(line)){
                return outputList;
            }

            String[] tokenList = line.split("\\s+|,|:|\\$|\\(|\\)");
            ArrayList<String> list = new ArrayList<String>();
            for (String s : tokenList){
                if (!s.equals("")){
                    list.add(s);
                }
            }

            tokenList = list.toArray(new String[list.size()]);
            for (int x = 0; x < tokenList.length; x++){
                //do checks to decide what to do with each token
                //check if label
                if(labelDict.get(tokenList[x]) != null){
                    continue;
                }
                //check if register
                else if(getRegister(tokenList[x]) != null){
                    continue;
                }
                //check if comment
                else if(tokenList[x].contains("#")){
                    return outputList;
                }
                else if(isInstruction2(tokenList[x])){
                    //check to see what instruction it is, error check, and append the opcode into the output string
                    if(tokenList[x].equals("add")){
                        //check that we have three valid registers after the add
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && getRegister(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("and")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && getRegister(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("or")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && getRegister(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("addi")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && isImmediate(tokenList[x+3]) != false){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("sll")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && isImmediate(tokenList[x+3]) != false){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("sub")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && getRegister(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("slt")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && getRegister(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("beq")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && labelDict.get(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("bne")){
                        if(getRegister(tokenList[x+1]) != null && getRegister(tokenList[x+2]) != null && labelDict.get(tokenList[x+3]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("lw")){
                        if(getRegister(tokenList[x+3]) != null && getRegister(tokenList[x+1]) != null && isImmediate(tokenList[x+2]) != false){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("sw")){
                        if(getRegister(tokenList[x+3]) != null && getRegister(tokenList[x+1]) != null && isImmediate(tokenList[x+2]) != false){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);
                            outputList.add(tokenList[x+2]);
                            outputList.add(tokenList[x+3]);
                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("j")){
                        if(labelDict.get(tokenList[x+1]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);

                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("jr")){
                        if(getRegister(tokenList[x+1]) != null){
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);

                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }
                    else if(tokenList[x].equals("jal")){
                        if(labelDict.get(tokenList[x+1]) != null) {
                            outputList.add(tokenList[x]);
                            outputList.add(tokenList[x+1]);

                            return outputList;
                        }else{
                            System.out.println("invalid syntax for instruction: " + tokenList[x]);
                            System.exit(0);
                        }
                    }

                }

                //since registers are handled above we just skip them when iterated
                return outputList;
            }


        }catch(Exception e2){
            e2.printStackTrace();
        }
        //return what should be the final line in binary
        return outputList;
    }
    //checks for blank lines
    public static boolean isBlankLine(String string){
        return string == null || string.trim().isEmpty();
    }
    //has functionality to exit invalid codeelse{

    public static boolean isInstruction2(String string){
        List<String> validInstructions = Arrays.asList("add","or","and","addi","sll","slt","beq","bne","lw","sw","j","jr","jal","sub");
        if(validInstructions.contains(string)){
            return true;
        }else{
            System.out.println("invalid instruction: " + string);
            System.exit(0);
            return false;
        }

    }

    public static boolean isInstruction(String string){
        List<String> validInstructions = Arrays.asList("add","or","and","addi","sll","slt","beq","bne","lw","sw","j","jr","jal","sub");
        if(validInstructions.contains(string)){
            return true;
        }else{
            return false;
        }

    }


    public static String getRegister(String reg) throws Exception{
        if(reg.equals("zero") || reg.equals("0")){
            return "00000";
        }
        else if (reg.equals("v0")){
            return"00010";
        }
        else if (reg.equals("v1")){
            return"00011";
        }
        else if (reg.equals("a0")){
            return"00100";
        }
        else if (reg.equals("a1")){
            return"00101";
        }
        else if (reg.equals("a2")){
            return"00110";
        }
        else if (reg.equals("a3")){
            return"00111";
        }
        else if (reg.equals("t0")){
            return"01000";
        }
        else if (reg.equals("t1")){
            return"01001";
        }
        else if (reg.equals("t2")){
            return"01010";
        }
        else if (reg.equals("t3")){
            return"01011";
        }
        else if (reg.equals("t4")){
            return"01100";
        }
        else if (reg.equals("t5")){
            return"01101";
        }
        else if (reg.equals("t6")){
            return"01110";
        }
        else if (reg.equals("t7")){
            return"01111";
        }
        else if (reg.equals("s0")){
            return"10000";
        }
        else if (reg.equals("s1")){
            return"10001";
        }
        else if (reg.equals("s2")){
            return"10010";
        }
        else if (reg.equals("s3")){
            return"10011";
        }
        else if (reg.equals("s4")){
            return"10100";
        }
        else if (reg.equals("s5")){
            return"10101";
        }
        else if (reg.equals("s6")){
            return"10110";
        }
        else if (reg.equals("s7")){
            return"10111";
        }
        else if (reg.equals("t8")){
            return"11000";
        }
        else if (reg.equals("t9")){
            return"11001";
        }
        else if (reg.equals("sp")){
            return"11101";
        }
        else if (reg.equals("ra")){
            return"11111";
        }
        else{
            return null;
        }
    }

    public static boolean isImmediate(String string){
        if(string == null){
            return false;
        }
        try{
            double d = Double.parseDouble(string);
        } catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public static String[] splitLine(String line){
        line = line.replaceAll(":(?!$)", ": ");
        line = line.replaceAll("#(?!$)", " #");
        String[] tokenList = line.split("\\s+|,|\\$|\\(|\\)");
        return tokenList;
    }

    public static String decimaltoImm(String imm){
        String bits = "0000000000000000";
        String immValue = Integer.toBinaryString(Integer.parseInt(imm));
        String[] immBits = immValue.split("");
        if (Integer.parseInt(imm) < 0) {
            return immValue.substring(16,32);
        }
        else {
            for (int i = 0;i <immBits.length; i++){
                if (immBits[i].equals("1")){

                    bits = bits.substring(0,15) + "1";
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }else {
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }
            }
        }
        return bits;
    }
    public static String decimaltoSham(String imm){
        String bits = "00000";
        String immValue = Integer.toBinaryString(Integer.parseInt(imm));
        String[] immBits = immValue.split("");
        if (Integer.parseInt(imm) < 0) {
            return immValue;
        }
        else {
            for (int i = 0;i <immBits.length; i++){
                if (immBits[i].equals("1")){

                    bits = bits.substring(0,4) + "1";
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }else {
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }
            }
        }
        return bits;
    }

    public static String decimaltoAddress(int addr){
        String bits = "00000000000000000000000000";
        String immValue = Integer.toBinaryString(addr);
        String[] immBits = immValue.split("");
        if (addr < 0) {
            return immValue.substring(6,32);
        }
        else {
            for (int i = 0;i <immBits.length; i++){
                if (immBits[i].equals("1")){

                    bits = bits.substring(0,25) + "1";
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }else {
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }
            }
        }
        return bits;
    }

    public static String branchAddress(int addr){
        String bits = "0000000000000000";
        String immValue = Integer.toBinaryString(addr);
        String[] immBits = immValue.split("");
        if (addr < 0) {
            return immValue.substring(16,32);
        }
        else {
            for (int i = 0;i <immBits.length; i++){
                if (immBits[i].equals("1")){

                    bits = bits.substring(0,25) + "1";
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }else {
                    if(i!= (immBits.length-1)) {
                        bits = bits.substring(1) + "0";
                    }
                }
            }
        }
        return bits;
    }
}