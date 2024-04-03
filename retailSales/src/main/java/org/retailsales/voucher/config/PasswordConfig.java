package org.retailsales.voucher.config;

import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.security.SecurityUtils;
import org.quincy.rock.core.util.StringUtil;
import org.retailsales.voucher.AppUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

/**
 * <b>密码加密配置。</b>
 * <p><b>详细说明：</b></p>
 * <!-- 在此添加详细说明 -->
 * 无。
 *
 * @author mex2000
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("deprecation")
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "encoder.crypt")
public class PasswordConfig {
	/**
	 * SECURE_KEY。
	 */
	public static final String SECURE_KEY_STRING = SecurityUtils.correctKey("org.retailSales.voucher.admin.user");
	/**
	 * SECURE_KEY。
	 */
	public static final byte[] SECURE_KEY = SECURE_KEY_STRING.getBytes(StringUtil.ISO_8859_1);

	private int strength = 10;
	private boolean enable = true;

	public void setUseCaptcha(boolean useCaptcha) {
		AppUtils.useCaptcha = useCaptcha;
	}

	public boolean getUseCaptcha() {
		return AppUtils.useCaptcha;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		if (enable) {
			return new BCryptPasswordEncoder(BCryptVersion.$2A, strength, new SecureRandom(SECURE_KEY));
		} else {
			return NoOpPasswordEncoder.getInstance();
		}
	}
}
