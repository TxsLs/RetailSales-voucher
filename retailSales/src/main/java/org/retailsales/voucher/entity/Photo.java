package org.retailsales.voucher.entity;


import lombok.Getter;
import lombok.Setter;
import org.quincy.rock.core.util.StringUtil;
import org.retailsales.voucher.Entity;

@Getter
@Setter
public class Photo extends Entity {
	/**
	 * serialVersionUID。
	 */
	private static final long serialVersionUID = -7109419962019911868L;

	private byte[] photo;
	private String photoFile;

	public long length() {
		return isEmpty() ? 0 : photo.length;
	}

	public boolean isEmpty() {
		return StringUtil.isBlank(photoFile) || photo == null || photo.length == 0;
	}

	public static boolean isEmpty(Photo photo) {
		return photo == null || photo.isEmpty();
	}
}
