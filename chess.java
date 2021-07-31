/**Copyright (c) , all rights reversed by the Authors
 * 
 * the last version caused printing problems and therefore now its updated and resolved
 * 
 * the cjks are now replaced with the greek letters
 * 
 */
import java.util.Scanner;
class chess
{
    /** 
     * The class {@code chess} aims at making player vs player chess game
     * It contains all methods that are necessary to accomplish the above task 
     * 
     *@Author -- Anuj Patel
     *@since -- 2021
     */

    //INSTANCES
    private static String[][] players;
    String board[][];//board
    String rotboard[][];//temp board
    private int f;//pointer for " check " or " checkmate " condition
    private int x;//checks if the pointer f repeats its value
    private String command[];//splitted command given by user
    /**
     * @param f ---- global but private var
     * 
     * f=-1 ====> niether check nor checkmate 
     * f=0 ====> check
     * f=1 ====> checkmate
     */
    /**
     * Default constructor to initialize the above instance variables
     * to their needed value
     */
    chess()
    {
        players=new String[2][2];
        board= new String[8][8];
        rotboard = new String[8][8];
        f=-1;
        x=0;
    }

    /**
     *
     * the below function will set the initial battle ground
     * Since awt  package is not included not of the players will get greek letters just for differentiating units
     * 
     * @param decided ---- derieved from toss which randomly chooses a player who will get greek letters
     * 
     */
    void setting(int decided)
    {

        //setting the initial battleground
        if(decided == 1)//If player 2 wins the toss(just to swap the names in the array)
        {
            players[1][0]="E";
            players[1][1]="G";
            rotate();
        }
        else
        {
            players[1][0]="G";
            players[1][1]="E";
        }
        int i=1;//row variable
        while(i<8)
        {
            for(int j=0;j<8;j++)//column variable loop
            { 
                //Assigning all the pawns
                if(i==1) 
                    board[i][j]="ι ";
                else if (i==6)
                    board[i][j]="P ";

                else
                    board[i][j]="  ";
            }
            i++;
        } 
        i=0;//var changed to column one for memory management
        while(i!=3)
        {
            //column pointer "i" showing the places of rook,bishop and knight.
            if(i==0)
            {   
                board[0][i]="Ω ";
                board[0][8-(i+1)]=board[0][i];
                board[7][i]="R ";
                board[7][8-(i+1)]=board[7][i];
            }
            if(i==1)
            {   
                board[0][i]="Γ ";
                board[0][8-(i+1)]=board[0][i];
                board[7][i]="Kn";
                board[7][8-(i+1)]=board[7][i];
            }
            if(i==2)
            {    
                board[0][i]="Δ ";
                board[0][8-(i+1)]=board[0][i];
                board[7][i]="B ";
                board[7][8-(i+1)]=board[7][i];
            }
            i++;
        }
        //placing the queens and kings at there positions
        board[0][3]="ϖ ";
        board[0][4]="Ψ ";
        board[7][3]="Q ";
        board[7][4]="K ";
    }

    private boolean isGreek(String unit)
    {
        boolean a=false;
        unit=unit.trim();
        if(unit.equals("Ψ")||unit.equals("Δ")||unit.equals("Γ")||unit.equals("Ω")||unit.equals("ι")||unit.equals("ϖ"))
            a=true;
        return a;
    }

    private boolean rules()
    {
        boolean go=true;
        rules_block:
        {
            int[] location={Integer.parseInt(String.valueOf(command[1].charAt(0))),Integer.parseInt(String.valueOf(command[1].charAt(2)))};
            String u="";
            if((players[1][1].equals("G")&& !isGreek(board[location[0]][location[1]].trim())/* &&!board[location[0]][location[1]].equals("  ") */)||(players[1][1].equals("E") && (isGreek(board[location[0]][location[1]].trim()) || board[location[0]][location[1]].equals("  "))))
            {
                if(board[location[0]][location[1]].equals("  "))
                {
                    System.out.println("NO UNIT FOUND AT THE GIVEN LOCATION\n\nTry Again");
                    go=false;
                    break rules_block;
                }
                else
                {
                    System.out.println("YOU CAN ONLY MOVE YOUR UNITS\n\nType The Command Again");
                    go=false;
                    break rules_block;
                }
            }
            //checking if the correct unit is specified.
            if(!((command[0].equals("P")&&(board[location[0]][location[1]].trim().equals("P")||board[location[0]][location[1]].trim().equals("ι"))) ||(command[0].equals("K")&&(board[location[0]][location[1]].trim().equals("K")||board[location[0]][location[1]].trim().equals("Ψ"))) ||(command[0].equals("Q")&&(board[location[0]][location[1]].trim().equals("ϖ")||board[location[0]][location[1]].trim().equals("Q"))) ||(command[0].equals("R")&&(board[location[0]][location[1]].trim().equals("R")||board[location[0]][location[1]].trim().equals("Ω"))) ||(command[0].equals("B")&&(board[location[0]][location[1]].trim().equals("B")||board[location[0]][location[1]].trim().equals("Δ"))) ||(command[0].equals("KN")&&(board[location[0]][location[1]].trim().equals("KN")||board[location[0]][location[1]].trim().equals("Γ"))) ))
            {
                System.out.println("The given unit is not present at the specified location\n\nTry Again");
                go=false;
                break rules_block;
            }
            boolean a;
            boolean b;
            if(command[0].equals("P")||command[0].equals("ι"))
            {
                //pawn in f=0 will go front and will only kill the units because of whom check has occured
                if(command[2].equals("F"))
                {
                    if(Integer.parseInt(command[3])<=2&&Integer.parseInt(command[3])>0)
                    {
                        if(Integer.parseInt(command[3])=="2"&& location[0]!=6)
                        {
                            System.out.println("Pawn cannot move two tiles unless its, its first move");
                            go=false;
                            break rules_block;
                        }
                        //checking if someone is present in between the path specified
                        for(int k=1;k<=Integer.parseInt(command[3]);k++)
                        {
                            a=isGreek(board[location[0]-k][location[1]]);
                            b=!board[location[0]-k][location[1]].equals("  ");
                            if(k<=Integer.parseInt(command[3]) && (a||(!a&&b)))
                            {
                                System.out.println("Invalid move\n\nTry Again");
                                go=false;
                                break rules_block;
                            }
                        }
                        movement();
                        check();
                        if(f==0)
                        {   
                            x=0;
                            System.out.println("movement not possible");
                            command[3]="-"+command[3];
                            movement();
                            go=false;
                            break rules_block;
                        }
                    }
                    else
                    {
                        System.out.println("the pawn can move 1 or 2 tile only\n\nTry again");                        
                        go=false;
                        break rules_block;
                    }
                }
                else if(command[2].equals("FL")||command[2].equals("FR")||command[2].equals("LF")||command[2].equals("RF"))
                {
                    if(Integer.parseInt(command[3])==1)
                    {
                        if(command[2].equals("FL")||command[2].equals("LF"))
                        {
                            if(board[location[0]-1][location[1]-1].equals("  "))
                            {
                                System.out.println("cannot move cross unless opponet unit present\n\nTry Again");
                                go=false;
                                break rules_block;
                            }
                            else
                            {
                                if(players[1][1].equals("G"))
                                {
                                    if(isGreek(board[location[0]-1][location[1]-1]))
                                    {
                                        System.out.println("move not allowed\n\nTry Again");
                                        go=false;
                                        break rules_block;
                                    }
                                    u=board[location[0]-1][location[1]-1];
                                    movement();
                                    check();
                                    if(f==0)
                                    {
                                        x=0;
                                        System.out.println("movement not possible");
                                        command[3]="-1";
                                        movement();
                                        board[location[0]-1][location[1]-1]=u;
                                        go=false;
                                        break rules_block;
                                    }
                                }
                                else
                                {
                                    if(!isGreek(board[location[0]-1][location[1]-1]))
                                    {
                                        System.out.println("move not allowed\n\nTry Again");   
                                        go=false;
                                        break rules_block;
                                    }
                                    u=board[location[0]-1][location[1]-1];
                                    movement();
                                    check();
                                    if(f==0)
                                    {   
                                        x=0;
                                        System.out.println("movement not possible");
                                        command[3]="-1";
                                        movement();
                                        board[location[0]-1][location[1]-1]=u;
                                        go=false;
                                        break rules_block;
                                    }
                                }
                            }   
                        }
                        else//RF and FR
                        {
                            if(board[location[0]-1][location[1]+1].equals("  "))
                            {
                                System.out.println("cannot move cross unless opponet unit present\n\nTry Again");
                                go=false;
                                break rules_block;
                            }
                            else
                            {
                                if(players[1][1].equals("G"))
                                {
                                    if(isGreek(board[location[0]-1][location[1]+1]))
                                    {
                                        System.out.println("move not allowed\n\nTry Again");
                                        go=false;
                                        break rules_block;
                                    }
                                    u=board[location[0]-1][location[1]+1];
                                    movement();
                                    check();
                                    if(f==0)
                                    {   
                                        x=0;
                                        System.out.println("movement not possible");
                                        command[3]="-1";
                                        movement();
                                        board[location[0]-1][location[1]+1]=u;
                                        go=false;
                                        break rules_block;
                                    }
                                }
                                else
                                {
                                    if(!isGreek(board[location[0]-1][location[1]+1]))
                                    {
                                        System.out.println("move not allowed\n\nTry Again");                                
                                        go=false;
                                        break rules_block;
                                    }
                                    board[location[0]-1][location[1]+1]=u;
                                    movement();
                                    check();
                                    if(f==0)
                                    {   
                                        x=0;
                                        System.out.println("movement not possible");
                                        command[3]="-1";
                                        movement();
                                        board[location[0]-1][location[1]+1]=u;
                                        go=false;
                                        break rules_block;
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("ERROR TILE NO. SHOULD BE 1\n\nTry Again");
                        go=false;
                        break rules_block;
                    }
                }
                else
                {
                    System.out.println("not allowed for pawn");
                    go=false;
                    break rules_block;
                }
            }
            else if(command[0].equals("KN")||command[0].equals("Γ"))
            {
                if(Integer.parseInt(command[3])!=1)
                {
                    System.out.println("invalid input\n\nNote: it's okay even if you do not write any no.\n\nTry again");
                    go=false;
                    break rules_block;
                }
                else
                {
                    //RF,FR,RB,BR,LF,FL,BL,LB
                    if(command[2].equals("RF")||command[2].equals("FR")||command[2].equals("LF")||command[2].equals("FL")||command[2].equals("RB")||command[2].equals("BR")||command[2].equals("LB")||command[2].equals("BL"))
                    {
                        if(command[2].equals("RF"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]-1][location[1]+2]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-1][location[1]+2]; 
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LB";
                                    movement();
                                    board[location[0]-1][location[1]+2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]-1][location[1]+2]) && !board[location[0]-1][location[1]+2].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-1][location[1]+2];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LB";
                                    movement();
                                    board[location[0]-1][location[1]+2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("FR"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]-2][location[1]+1]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");   
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-2][location[1]+1];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="BL";
                                    movement();
                                    u=board[location[0]-2][location[1]+1];
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]-2][location[1]+1]) && !board[location[0]-2][location[1]+1].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-2][location[1]+1];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="BL";
                                    movement();
                                    board[location[0]-2][location[1]+1]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("FL"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]-2][location[1]-1]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-2][location[1]-11];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="BR";
                                    movement();
                                    board[location[0]-2][location[1]-1]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]-2][location[1]-1]) && !board[location[0]-2][location[1]-1].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-2][location[1]-1];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="BR";
                                    movement();
                                    board[location[0]-2][location[1]-1]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("LF"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]-1][location[1]-2]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-1][location[1]-2];
                                movement();
                                check();
                                if(f==0)
                                {   
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="RB";
                                    movement();
                                    board[location[0]-1][location[1]-2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]-1][location[1]-2]) && !board[location[0]-1][location[1]-2].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-1][location[1]-2];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="RB";
                                    movement();
                                    board[location[0]-1][location[1]-2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("BL"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]+2][location[1]-1]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+2][location[1]-1];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="FR";
                                    movement();
                                    board[location[0]+2][location[1]-1]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]+2][location[1]-1]) && !board[location[0]+2][location[1]-1].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+2][location[1]-1];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="FR";
                                    movement();
                                    board[location[0]+2][location[1]-1]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("LB"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]+1][location[1]-2]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+1][location[1]-2];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="RF";
                                    movement();
                                    board[location[0]+1][location[1]-2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]+1][location[1]-2]) && !board[location[0]+1][location[1]-2].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+1][location[1]-2];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="RF";
                                    movement();
                                    board[location[0]+1][location[1]-2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else if(command[2].equals("RB"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]+1][location[1]+2]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+1][location[1]+2];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LF";
                                    movement();
                                    board[location[0]+1][location[1]+2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!isGreek(board[location[0]+1][location[1]+2]) && !board[location[0]+1][location[1]+2].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+1][location[1]+2];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LF";
                                    movement();
                                    board[location[0]+1][location[1]+2]=u;
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                        else// (command[2].equals("BR"))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(isGreek(board[location[0]+2][location[1]+1]))//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+2][location[1]+1];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LF";
                                    movement();
                                    board[location[0]+2][location[1]+1]=u;
                                    go=false;
                                    break rules_block;
                                }    
                            }
                            else
                            {
                                if(!isGreek(board[location[0]+2][location[1]+1]) && !board[location[0]+2][location[1]+1].equals("  "))//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+2][location[1]+1];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    System.out.println("movement not possible");
                                    command[2]="LF";
                                    movement();
                                    board[location[0]+2][location[1]+1]=u;
                                    go=false;
                                    break rules_block;
                                }  
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Not allowed for Knight\n\nTry Again");
                        go=false;
                        break rules_block;
                    }
                }
            }        
            else if(command[0].equals("K")||command[0].equals("Ψ"))
            {

                //f,b,l,r,fl,fr,bl,br just one step
                if(Integer.parseInt(command[3])==1)
                {
                    if(command[2].equals("F"))
                    {
                        a=isGreek(board[location[0]-1][location[1]]);
                        b=!board[location[0]-1][location[1]].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("B"))
                    {
                        a=isGreek(board[location[0]+1][location[1]]);
                        b=!board[location[0]+1][location[1]].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("R"))
                    {
                        a=isGreek(board[location[0]][location[1]+1]);
                        b=!board[location[0]][location[1]+1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("L"))
                    {
                        a=isGreek(board[location[0]][location[1]-1]);
                        b=!board[location[0]][location[1]-1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("FL")||command[2].equals("LF"))
                    {
                        a=isGreek(board[location[0]-1][location[1]-1]);
                        b=!board[location[0]-1][location[1]-1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("FR")||command[2].equals("RF"))
                    {
                        a=isGreek(board[location[0]-1][location[1]+1]);
                        b=!board[location[0]-1][location[1]+1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]-1][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]-1][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("BL")||command[2].equals("LB"))
                    {
                        a=isGreek(board[location[0]+1][location[1]-1]);
                        b=!board[location[0]+1][location[1]-1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]-1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]-1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else if(command[2].equals("BR")||command[2].equals("RB"))
                    {
                        a=isGreek(board[location[0]+1][location[1]+1]);
                        b=!board[location[0]+1][location[1]+1].equals("  ");
                        if(players[1][1].equals("G"))
                        {
                            if(a)//same team
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                        else
                        {
                            if(!a&&b)
                            {
                                System.out.println("movement not possible");
                                go=false;
                                break rules_block;
                            }
                            u=board[location[0]+1][location[1]+1];
                            movement();
                            check();
                            if(f==0)
                            {
                                x=0;
                                command[3]="-1";
                                movement();
                                System.out.println("Movement not possible");
                                board[location[0]+1][location[1]+1]=u;
                                go=false;
                                break rules_block;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Invalid movement type\n\nTry Again");
                        go=false;
                        break rules_block;
                    }
                }
                else
                {
                    System.out.println("invalis disp. number\n\nTry Again");
                    go=false;
                    break rules_block;
                }
            }
            else if(command[0].equals("B")||command[0].equals("Δ"))
            {
                for(int k=1;k<=Integer.parseInt(command[3]);k++)
                {
                    if(command[2].equals("FL")||command[2].equals("LF"))
                    {
                        a=isGreek(board[location[0]-k][location[1]-k]);
                        b=!board[location[0]-k][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("LB")||command[2].equals("BL"))
                    {
                        a=isGreek(board[location[0]+k][location[1]-k]);
                        b=!board[location[0]+k][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    break rules_block;
                                }
                                u=board[location[0]+k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team but not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block; 
                                }

                                u=board[location[0]+k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("RB")||command[2].equals("BR"))
                    {
                        a=isGreek(board[location[0]+k][location[1]+k]);
                        b=!board[location[0]+k][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("FR")||command[2].equals("RF"))
                    {
                        a=isGreek(board[location[0]-k][location[1]+k]);
                        b=!board[location[0]-k][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("not allowed for Bishop\n\nTry Again");
                        go=false;
                        break rules_block;
                    }
                }
            }
            else if(command[0].equals("R")||command[0].equals("Ω"))
            {
                for(int k=1;k<=Integer.parseInt(command[3]);k++)
                {
                    if(command[2].equals("F"))
                    {
                        a=isGreek(board[location[0]-k][location[1]]);
                        b=!board[location[0]-k][location[1]].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("B"))
                    {
                        a=isGreek(board[location[0]+k][location[1]]);
                        b=!board[location[0]+k][location[1]].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("L"))
                    {
                        a=isGreek(board[location[0]][location[1]-k]);
                        b=!board[location[0]][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("R"))
                    {
                        a=isGreek(board[location[0]][location[1]+k]);
                        b=!board[location[0]][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                    }

                    else
                    {
                        System.out.println("Not allowed in case of Rook\n\nTry Again");
                        go=false;
                        break rules_block;
                    }

                }
            }
            else if(command[0].equals("Q")||command[0].equals("ϖ"))
            {
                //F,B,L,R,(FL,LF),(LB,BL),(FR,RF),(BR,RB)
                for(int k=1;k<=Integer.parseInt(command[3]);k++)
                {
                    if(command[2].equals("F"))
                    {
                        a=isGreek(board[location[0]-k][location[1]]);
                        b=!board[location[0]-k][location[1]].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("B"))
                    {
                        a=isGreek(board[location[0]+k][location[1]]);
                        b=!board[location[0]+k][location[1]].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("L"))
                    {
                        a=isGreek(board[location[0]][location[1]-k]);
                        b=!board[location[0]][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("R"))
                    {
                        a=isGreek(board[location[0]][location[1]+k]);
                        b=!board[location[0]][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                    }

                    else if(command[2].equals("FL")||command[2].equals("LF"))
                    {
                        a=isGreek(board[location[0]-k][location[1]-k]);
                        b=!board[location[0]-k][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]-k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("LB")||command[2].equals("BL"))
                    {
                        a=isGreek(board[location[0]+k][location[1]-k]);
                        b=!board[location[0]+k][location[1]-k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team but not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]-k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]-k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("RB")||command[2].equals("BR"))
                    {
                        a=isGreek(board[location[0]+k][location[1]+k]);
                        b=!board[location[0]+k][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]+k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }
                                u=board[location[0]+k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]+k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                        }
                    }
                    else if(command[2].equals("FR")||command[2].equals("RF"))
                    {
                        a=isGreek(board[location[0]-k][location[1]+k]);
                        b=!board[location[0]-k][location[1]+k].equals("  ");
                        if(k<Integer.parseInt(command[3]) && (a||(!a&&b)))
                        {
                            System.out.println("Invalid move\n\nTry Again");
                            go=false;
                            break rules_block;
                        }
                        if(k==Integer.parseInt(command[3]))
                        {
                            if(players[1][1].equals("G"))
                            {
                                if(a)//same team
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }

                            }
                            else
                            {
                                if(!a && b)//same team and not spaces
                                {
                                    System.out.println("move not possible\n\nTry Again");
                                    go=false;
                                    break rules_block;
                                }

                                u=board[location[0]-k][location[1]+k];
                                movement();
                                check();
                                if(f==0)
                                {
                                    x=0;
                                    command[3]="-"+command[3];
                                    movement();
                                    board[location[0]-k][location[1]+k]=u;
                                    System.out.println("movement not possible");
                                    go=false;
                                    break rules_block;
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Not Allowed for Queen\n\nTry Again");
                        go=false;
                        break rules_block;
                    }                
                }
            }
            else
            {
                System.out.println("Invalid units name\n\nTry Again");
            }
        }
        return go;
    }

    int toss()//toss in order to decide which player gets symbolic units
    {
        int a= (int)(2*Math.random());
        return a;//returns 0 or 1
    }

    void accept()
    {
        Scanner obj= new Scanner(System.in);
        System.out.println("Enter the command");
        String order =obj.nextLine();
        separator(order); 
    }

    /**
     * This function will separate the commands via split function
     * @param a = the command from accept()
     */
    void separator(String a)
    {
        //Moreover in the next version efforts will be made that "no delimited command" can also be processed
        //thinking is must for the above
        a=a.trim().toUpperCase();
        if((a.startsWith("KN") && !(Character.isDigit(a.charAt(a.length()-1)))) || ((a.startsWith("P") && !Character.isDigit((a.charAt(a.length()-1))))))
            a=a+" 1";
        command=a.split(" ");
    }

    private boolean ExceptionFind()
    {
        try
        {
            String a;
            if(command[0].equals("KN"))
            {
                if(command[2].equals("LF"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-1][Integer.parseInt(command[1].substring(2))-2];
                }
                else if(command[2].equals("FL"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-2][Integer.parseInt(command[1].substring(2))-1];
                }
                else if(command[2].equals("BL"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+2][Integer.parseInt(command[1].substring(2))-1];
                }
                else if(command[2].equals("LB"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+1][Integer.parseInt(command[1].substring(2))-2];
                }
                else if(command[2].equals("FR"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-2][Integer.parseInt(command[1].substring(2))+1];
                }
                else if(command[2].equals("RF"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-1][Integer.parseInt(command[1].substring(2))+2];
                }
                else if(command[2].equals("RB"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+1][Integer.parseInt(command[1].substring(2))+2];
                }
                else if(command[2].equals("BR"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+2][Integer.parseInt(command[1].substring(2))+1];
                }
            }
            else
            {
                if(command[2].equals("F"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))];
                }
                else if(command[2].equals("B"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))];
                }
                else if(command[2].equals("R"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))][Integer.parseInt(command[1].substring(2))+Integer.parseInt(command[3])];
                }
                else if(command[2].equals("L"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))][Integer.parseInt(command[1].substring(2))-Integer.parseInt(command[3])];
                }
                else if(command[2].equals("LF")||command[2].equals("FL"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))-Integer.parseInt(command[3])];
                }
                else if(command[2].equals("LB")||command[2].equals("BL"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))-Integer.parseInt(command[3])];
                }
                else if(command[2].equals("RF")||command[2].equals("FR"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))-Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))+Integer.parseInt(command[3])];
                }
                else if(command[2].equals("RB")||command[2].equals("BR"))
                {
                    a=board[Integer.parseInt(command[1].substring(0,1))+Integer.parseInt(command[3])][Integer.parseInt(command[1].substring(2))+Integer.parseInt(command[3])];
                }
            }
        }
        catch(Exception ArrayOutOfBound)
        {
            System.out.println("displacement no. exceeds the board \n\nTry Again");
            return false;
        }
        return true;
    }

    /**
     * This method would carry out movement in the units as per the command written
     * @param command --- it would contain all necessary information about the movement   
     */

    void movement()
    {
        int[] location={Integer.parseInt(String.valueOf(command[1].charAt(0))),Integer.parseInt(String.valueOf(command[1].charAt(2)))};
        int distile=Integer.parseInt(command[3]);
        if(command[2].equals("F"))
        {
            board[location[0]-distile][location[1]]=board[location[0]][location[1]];
            board[location[0]][location[1]]="  ";
        }
        else if(command[2].equals("B"))
        {
            board[location[0]+distile][location[1]]=board[location[0]][location[1]];
            board[location[0]][location[1]]="  ";
        }
        else if(command[2].equals("R"))
        {
            board[location[0]][location[1]+distile]=board[location[0]][location[1]];
            board[location[0]][location[1]]="  ";
        }
        else if(command[2].equals("L"))
        {
            board[location[0]][location[1]-distile]=board[location[0]][location[1]];
            board[location[0]][location[1]]="  ";
        }
        else if(command[2].equals("LF")||command[2].equals("FL"))
        {
            if(!command[0].equals("KN"))
            {
                board[location[0]-distile][location[1]-distile]=board[location[0]][location[1]];
                board[location[0]][location[1]]="  ";
            }
            else
            {
                if(command[2].equals("FL"))
                {
                    board[location[0]-2][location[1]-1]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
                else//LF
                {
                    board[location[0]-1][location[1]-2]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
            }
        }
        else if(command[2].equals("LB")||command[2].equals("BL"))
        {
            if(!command[0].equals("KN"))
            {
                board[location[0]+distile][location[1]-distile]=board[location[0]][location[1]];
                board[location[0]][location[1]]="  ";
            }
            else
            {
                if(command[2].equals("LB"))
                {
                    board[location[0]+1][location[1]-2]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
                else//BL
                {
                    board[location[0]+2][location[1]-1]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
            }
        }
        else if(command[2].equals("RB")||command[2].equals("BR"))
        {
            if(!command[0].equals("KN"))
            {
                board[location[0]+distile][location[1]+distile]=board[location[0]][location[1]];
                board[location[0]][location[1]]="  ";
            }
            else
            {
                if(command[2].equals("RB"))
                {
                    board[location[0]+1][location[1]+2]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
                else//BR
                {
                    board[location[0]+2][location[1]+1]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
            }
        }
        else if(command[2].equals("RF")||command[2].equals("FR"))
        {
            if(!command[0].equals("KN"))
            {
                board[location[0]-distile][location[1]+distile]=board[location[0]][location[1]];
                board[location[0]][location[1]]="  ";
            }
            else
            {
                if(command[2].equals("RF"))
                {
                    board[location[0]-1][location[1]-2]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
                else//FR
                {
                    board[location[0]-2][location[1]-1]=board[location[0]][location[1]];
                    board[location[0]][location[1]]="  ";
                }
            }
        }

    }

    void check()//This function checks whether the condition is normal or a check.
    {
        //when fxn is called after turn of player to know that if opponet's king is in trouble or not
        int[] kloc=new int[2];//getting king's location,the one in danger
        String o[][]=new String[2][16];//opponents and their location
        int k=0;//pointer from where null portion starts if any
        if(players[1][1].equals("E"))
        {
            //Finding E's King
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if((board[i][j].trim()).equals("K"))
                    {
                        kloc[0]=i;
                        kloc[1]=j;
                    }
                }
            }
            //Finding the opponents and their location that is G
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(isGreek(board[i][j].trim()))
                    {
                        o[0][k]=board[i][j].trim();
                        o[1][k]=i+","+j;
                        k++;
                    }
                }
            }
        }
        else//it was 'E' turn and King of 'G' may be in danger and no rotation has occured
        {
            //Finding G's king
            //implies find "Ψ" 
            //o will contain "KN" and others
            for(int i=0;i<8;i++)//finding the greek king
            {
                for(int j=0;j<8;j++)
                {
                    if((board[i][j].trim()).equals("Ψ"))
                    {
                        kloc[0]=i;
                        kloc[1]=j;
                    }
                }
            }    
            for(int i=0;i<8;i++)//finding the opponents that is E
            {
                for(int j=0;j<8;j++)
                {
                    if(board[i][j].trim().equals("Q")||board[i][j].trim().equals("R")||board[i][j].trim().equals("KN")||board[i][j].trim().equals("B")||board[i][j].trim().equals("P"))
                    {
                        o[0][k]=board[i][j].trim();
                        o[1][k]=i+","+j;
                        k++;
                    }
                }
            }

        } 

        k=0;
        String a;
        // A BLOCK WITH NAME Exception starts
        Exception:
        try
        {
            for(;k<16;k++)
                a=o[1][k].substring(0,1);
        }
        catch(Exception NullPointerException)
        {
            break Exception;
        }
        //Exception block ends and another block - loop starts
        loop:
        for(int i=0;i<k;i++)
        {
            int[] oloc={Integer.parseInt(o[1][i].substring(0,1)),Integer.parseInt(o[1][i].substring(2))};
            if(o[0][i].equals("Γ")||o[0][i].equals("KN"))
            {
                //eight derieved movements
                //eg: fl,lf,fr,rf,bl,lb,br,rb.
                if((kloc[0]-oloc[0]==-2&&kloc[1]-oloc[1]==-1)||(kloc[0]-oloc[0]==2&&kloc[1]-oloc[1]==-1) ||(kloc[0]-oloc[0]==-2&&kloc[1]-oloc[1]==1) ||(kloc[0]-oloc[0]==2&&kloc[1]-oloc[1]==1) ||(kloc[0]-oloc[0]==-1&&kloc[1]-oloc[1]==-2) ||(kloc[0]-oloc[0]==1&&kloc[1]-oloc[1]==2) ||(kloc[0]-oloc[0]==1&&kloc[1]-oloc[1]==-2) ||(kloc[0]-oloc[0]==-1&&kloc[1]-oloc[1]==2))
                {
                    if(f==0)
                        x++;
                    f=0;
                    if(x==0)
                        System.out.println("Its a check to "+players[0][0]);

                }
                else
                { 
                    f=-1;
                }
            }
            else
            {
                if(o[0][i].equals("ι")||o[0][i].equals("P"))//pawn
                {
                    //can give check diagonally , only by fl or fr
                    if(kloc[0]-oloc[0]==1&&Math.abs(kloc[1]-oloc[1])==1)
                    {
                        if(f==0)
                        {
                            x++;
                        }
                        f=0;
                        if(x==0)
                            System.out.println("Its a check to "+players[0][0]);
                    }
                    else
                    {
                        f=-1;
                    }
                }
                else if(o[0][i].equals("Δ")||o[0][i].equals("B"))//bishop
                {
                    //fl,fr,bl,br any tile no. but need to find that no one is b/w them
                    if(Math.abs(kloc[0]-oloc[0])==Math.abs(kloc[1]-oloc[1]))
                    {
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[0]-oloc[0]);t++)
                        {
                            if(kloc[0]<oloc[0]&&kloc[1]>oloc[1])//fr
                            {
                                if(board[oloc[0]-t][oloc[1]+t].equals("  "))
                                    count++;
                            }
                            else if(oloc[0]<kloc[0]&&oloc[1]>kloc[1])//bl
                            {
                                if(board[oloc[0]+t][oloc[1]-t].equals("  "))
                                {
                                    count++;
                                }
                            }
                            else if(oloc[0]>kloc[0]&&oloc[1]>kloc[1])//fl
                            {
                                if(board[oloc[0]-t][oloc[1]-t].equals("  "))
                                    count++;
                            }
                            else//br
                            {
                                if(board[oloc[0]+t][oloc[1]+t].equals("  "))
                                    count++;
                            }
                        }
                        if(count==Math.abs(kloc[0]-oloc[0]-1))
                        {
                            //System.out.println("Its check to "+players[0][0]);
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }
                        else
                        {
                            f=-1;
                        }
                    }
                }
                else if(o[0][i].equals("Ω")||o[0][i].equals("R"))//rook
                {
                    //f,b,r,l
                    if(kloc[1]-oloc[1]==0)
                    {
                        //implies f or b
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[0]-oloc[0]);t++)
                        {
                            if(kloc[0]>oloc[0])//implies b for units
                            {
                                if(board[oloc[0]+t][oloc[1]].equals("  "))
                                {
                                    count++;
                                }
                            }
                            else //implies f
                            {

                                if(board[oloc[0]-t][oloc[1]].equals("  "))
                                {
                                    count++;
                                }
                            }
                        }
                        if(count==Math.abs(kloc[0]-oloc[0])-1)
                        {
                            //System.out.println("Its a check to "+players[0][0]);
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }
                        else
                        {
                            f=-1;
                        }
                    }
                    else if(kloc[0]-oloc[0]==0)
                    {
                        //implies r,l
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[1]-oloc[1]);t++)
                        {
                            if(oloc[1]<kloc[1])//implies r
                            {
                                if(board[oloc[0]][oloc[1]+t].equals("  "))
                                    count++;
                            }
                            else//implies l
                            {
                                if(board[oloc[0]][oloc[1]-t].equals("  "))
                                    count++;
                            }
                        }
                        if(count==Math.abs(kloc[1]-oloc[1])-1)
                        {
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }
                        else
                        {    f=-1;}
                    }
                }
                else if(o[0][i].equals("ϖ")||o[0][i].equals("Q"))//queen
                {
                    //f,l,r,b.fl.fr,bl,br
                    //implies rook and bishop checking copy-paste
                    //copied from rook
                    //fundamental movement eg:f,b,r,l
                    if(kloc[1]-oloc[1]==0)
                    {
                        //implies f or b
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[0]-oloc[0]);t++)
                        {
                            if(kloc[0]>oloc[0])//implies b for units
                            {
                                if(board[oloc[0]+t][oloc[1]].equals("  "))
                                {
                                    count++;
                                }
                            }
                            else //implies f
                            {

                                if(board[oloc[0]-t][oloc[1]].equals("  "))
                                {
                                    count++;
                                }
                            }
                        }
                        if(count==Math.abs(kloc[0]-oloc[0])-1)
                        {
                            //System.out.println("Its a check to "+players[0][0]);
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }    
                        else
                        {    f=-1;}
                    }
                    else if(kloc[0]-oloc[0]==0)
                    {
                        //implies r,l
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[1]-oloc[1]);t++)
                        {
                            if(oloc[1]<kloc[1])//implies r
                            {
                                if(board[oloc[0]][oloc[1]+t].equals("  "))
                                    count++;
                            }
                            else//implies l
                            {
                                if(board[oloc[0]][oloc[1]-t].equals("  "))
                                    count++;
                            }
                        }
                        if(count==Math.abs(kloc[1]-oloc[1])-1)
                        {
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }
                        else    
                        {    f=-1;}
                    }
                    //copied from bishop
                    //derieved movement eg:fl
                    if(Math.abs(kloc[0]-oloc[0])==Math.abs(kloc[1]-oloc[1]))
                    {
                        int count=0;
                        for(int t=1;t<Math.abs(kloc[0]-oloc[0]);t++)
                        {
                            if(kloc[0]<oloc[0]&&kloc[1]>oloc[1])//fr
                            {
                                if(board[oloc[0]-t][oloc[1]+t].equals("  "))
                                    count++;
                            }
                            else if(oloc[0]<kloc[0]&&oloc[1]>kloc[1])//bl
                            {
                                if(board[oloc[0]+t][oloc[1]-t].equals("  "))
                                {
                                    count++;
                                }
                            }
                            else if(oloc[0]>kloc[0]&&oloc[1]>kloc[1])//fl
                            {
                                if(board[oloc[0]-t][oloc[1]-t].equals("  "))
                                    count++;
                            }
                            else//br
                            {
                                if(board[oloc[0]+t][oloc[1]+t].equals("  "))
                                    count++;
                            }
                        }
                        if(count==Math.abs(kloc[0]-oloc[0]-1))
                        {
                            //System.out.println("Its check to "+players[0][0]);
                            if(f==0)
                                x++;
                            f=0;
                            if(x==0)
                                System.out.println("Its a check to "+players[0][0]);
                        }
                        else    
                        {f=-1;}
                    }
                }
            }
        }
    }

    void checkmate()
    {
        //king will be focused on but the twist is that the king will be allowed to move that kloc will be changed 
        //this fxn imagines that the king moves -- ig i would need a temp board
        //and if it moves is there any potential risk
        //if the king cannot move a single tile then its checkmate 
        //when fxn is called after turn of player to know that if opponet's king is in trouble or not

        int[] kloc=new int[2];//getting king's location,the one in danger
        if(players[1][1].equals("G"))//G's turn has just been completed and therefore king of E may be in trouble 
        {
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if((board[i][j].trim()).equals("K"))
                    {
                        kloc[0]=i;
                        kloc[1]=j;
                    }
                }
            }
        }
        else//it was 'E' turn and King of 'G' may be in danger and no rotation has occured
        {
            for(int i=0;i<8;i++)//finding the greek king
            {
                for(int j=0;j<8;j++)
                {
                    if((board[i][j].trim()).equals("Ψ"))
                    {
                        kloc[0]=i;
                        kloc[1]=j;
                    }
                }
            }    
        } 
        //the above one is just copy-paste of check func

        //actual code starts
        int count=0;
        int r=kloc[0],c=kloc[1];
        loop:
        for(int i=r-1;i<=r+1;i++)
        {
            for(int j=c-1;j<=c+1;j++)
            {
                if(i>=0&&i<8&&j<8&&j>=0&&i!=r&&j!=c)
                {
                    if(board[i][j].equals("  "))
                    {
                        board[i][j]="K ";
                        board[kloc[0]][kloc[1]]="  ";
                        kloc[0]=i;
                        kloc[1]=j;
                        check();
                        count++;
                    }
                }
            }
        }
        //may get an exception or a logical error
        board[kloc[0]][kloc[1]]="  ";
        board[r][c]="K ";
        if(count==x)
            f=1;
        x=0;
    }

    void display_board()
    {
        String tmp="                        ";
        String temp=tmp;
        tmp=tmp.substring((24-players[0][0].length())/2);
        temp=temp.substring((24-players[0][1].length())/2);
        System.out.println(tmp+players[0][0]+tmp);
        System.out.println("   0 |1 |2 |3 |4 |5 |6 |7 |");
        for(int i=0;i<8;i++)
        {
            System.out.print(i+" |");
            for(int j=0;j<8;j++)
            {   
                System.out.print(board[i][j]+"|");
            }
            System.out.println();
        }
        System.out.println(temp+players[0][1]+temp);
    }

    public void rotate()
    {
        //rotating as per the movers view
        for(int i=board.length-1,c=0;i>=0;i--,c++)
        {
            for(int j=board.length-1,d=0;j>=0;j--,d++)
            {
                rotboard[c][d]=board[i][j];
            }
        }
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                board[i][j]=rotboard[i][j];
            }
        }
        //Swapping the players name and the units type info
        String temp =players[0][0];
        players[0][0]=players[0][1];
        players[0][1]=temp;
        temp=players[1][0];
        players[1][0]=players[1][1];
        players[1][1]=temp;
    }

    public static void main()
    {
        chess pg=new chess();
        Scanner s=new Scanner(System.in);
        System.out.print("Enter name\nPlayer 1:");
        players[0][0]=s.nextLine();
        System.out.print("Enter name\nPlayer 2:");
        players[0][1]=s.nextLine();
        int a=pg.toss();
        pg.setting(a);
        pg.display_board();
        loop:
        while(true)
        {
            pg.accept();
            if(pg.command.length!=4)
            {
                System.out.println("Invalid command\n\nTry Again");
                continue loop;
            }
            boolean b=pg.ExceptionFind();
            if(b==false)
            {
                continue loop;
            }
            boolean c=pg.rules();
            if(c==false)
                continue loop;
            pg.display_board();
            for(int i=-1111111111;i>=1000000000;i++)
            {}
            pg.rotate();
            pg.check();
            if(pg.f==0)
                pg.checkmate();
            if(pg.f==1)
                break loop;
            System.out.println('\f');
            pg.display_board();
        }
        System.out.println(players[1][1]+"wins");
        System.out.println("\f"+"PRESS ANY KEY TO REPLAY AND E TO EXIT");
        if(Character.toUpperCase(s.next().charAt(0))=='E')
            System.exit(0);
        else
            chess.main();
    }
}