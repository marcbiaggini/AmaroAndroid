package amaro.amaroandroid.Helpers;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by juan.villa on 31/05/2016.
 */
public class CpfHelper {

  private int randomiza(int n) {
    int ranNum = (int) (Math.random() * n);
    return ranNum;
  }

  private int mod(int dividendo, int divisor) {
    return (int) Math.round(dividendo - (Math.floor(dividendo / divisor) * divisor));
  }

  public String setCPF() {
    int n = 9;
    int n1 = randomiza(n);
    int n2 = randomiza(n);
    int n3 = randomiza(n);
    int n4 = randomiza(n);
    int n5 = randomiza(n);
    int n6 = randomiza(n);
    int n7 = randomiza(n);
    int n8 = randomiza(n);
    int n9 = randomiza(n);
    int d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;

    d1 = 11 - (mod(d1, 11));

    if (d1 >= 10)
      d1 = 0;

    int d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;

    d2 = 11 - (mod(d2, 11));

    String retorno = null;

    if (d2 >= 10) {
      d2 = 0;
    }
    retorno = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2;
    return retorno;
  }

  public boolean isValidCPF(String CPF) {

    // considera-se erro CPF's formados por uma sequencia de numeros iguais
    if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11))
      return (false);

    char dig10, dig11;
    int add, i, rev, num, peso;

    // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
    try {
      // Valida 1o digito
      add = 0;
      peso = 10;
      for (i = 0; i < 9; i++) {
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere '0' no inteiro 0
        // (48 eh a posicao de '0' na tabela ASCII)
        num = (int) (CPF.charAt(i) - 48);
        add = add + (num * peso);
        peso = peso - 1;
      }

      rev = 11 - (add % 11);
      if ((rev == 10) || (rev == 11))
        dig10 = '0';
      else
        dig10 = (char) (rev + 48); // converte no respectivo caractere numerico

      // Calculo do 2o. Digito Verificador
      add = 0;
      peso = 11;
      for (i = 0; i < 10; i++) {
        num = (int) (CPF.charAt(i) - 48);
        add = add + (num * peso);
        peso = peso - 1;
      }

      rev = 11 - (add % 11);
      if ((rev == 10) || (rev == 11))
        dig11 = '0';
      else
        dig11 = (char) (rev + 48);

      // Verifica se os digitos calculados conferem com os digitos informados.
      if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
        return (true);
      else
        return (false);
    } catch (InputMismatchException erro) {
      return (false);
    }
  }

}
