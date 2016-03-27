package com.maranhon.server.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * N�o depende de concorr�ncia, ent�o n�o � relacionado ao PBL. 
 * Fonte: http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string/41156#41156
 * @author Joel. Ou n�o.
 *
 */

public class RequestIDGenerator {
	  private SecureRandom random = new SecureRandom();

	  public String nextID() {
	    return new BigInteger(130, random).toString(32);
	  }
}
