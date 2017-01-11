/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apicall;

import com.mycompany.loriamusic.Application;
import org.springframework.boot.SpringApplication;

/**
 *
 * @author Yohann
 */
public class Mian {

    public static void main(String[] args) {
        System.out.println(Youtube.search("Lorde Royals"));
    }
}
