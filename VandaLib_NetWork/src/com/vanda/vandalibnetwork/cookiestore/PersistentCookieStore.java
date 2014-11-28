package com.vanda.vandalibnetwork.cookiestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

public class PersistentCookieStore implements CookieStore {
	private static final String LOG_TAG = "PersistentCookieStore";
	private static final String COOKIE_PREFS = "CookiePrefsFile";
	private static final String COOKIE_NAME_STORE = "names";
	private static final String COOKIE_NAME_PREFIX = "cookie_";
	private boolean omitNonPersistentCookies = false;

	private final ConcurrentHashMap<String, Cookie> cookies;
	private final SharedPreferences cookiePrefs;

	/**
	 * Construct a persistent cookie store.
	 * 
	 * @param context
	 *            Context to attach cookie store to
	 */
	public PersistentCookieStore(Context context) {
		cookiePrefs = context.getSharedPreferences(COOKIE_PREFS,
				Activity.MODE_PRIVATE);
		cookies = new ConcurrentHashMap<String, Cookie>();

		// Load any previously stored cookies into the store
		String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE,
				null);
		if (storedCookieNames == null) {
			Log.i("Cookies", "clean cookie");
		}
		if (storedCookieNames != null) {
			String[] cookieNames = TextUtils.split(storedCookieNames, ",");
			for (String name : cookieNames) {
				String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX
						+ name, null);
				if (encodedCookie != null) {
					Cookie decodedCookie = decodeCookie(encodedCookie);
					if (decodedCookie != null) {
						cookies.put(name, decodedCookie);
						// Editor et = cookiePrefs.edit();
						// et.putBoolean("Login", true);
						// et.commit();
					}
				}
			}

			// Clear out expired cookies
			clearExpired(new Date());
		}
	}

	@Override
	public void addCookie(Cookie cookie) {
		Log.i("Cookies", "addCookie");
		if (omitNonPersistentCookies && !cookie.isPersistent())
			return;
		String name = cookie.getName() + cookie.getDomain();
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		// Save cookie into local store, or remove if expired
		if (!cookie.isExpired(new Date())) {
			cookies.put(name, cookie);
		} else {
			cookies.remove(name);
		}

		// Save cookie into persistent store

		prefsWriter.putString(COOKIE_NAME_STORE,
				TextUtils.join(",", cookies.keySet()));
		prefsWriter.putString(COOKIE_NAME_PREFIX + name,
				encodeCookie(new SerializableCookie(cookie)));
		prefsWriter.commit();
	}

	@Override
	public void clear() {
		Log.i("Cookies", "clear");
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		prefsWriter.remove("Login");// ("login", false);
		for (String name : cookies.keySet()) {
			prefsWriter.remove(COOKIE_NAME_PREFIX + name);
		}
		prefsWriter.remove(COOKIE_NAME_STORE);
		prefsWriter.commit();
		cookies.clear();
	}

	public boolean getLoginStatus() {
		boolean is = false;
		if (cookiePrefs.contains("Login")) {
			is = cookiePrefs.getBoolean("Login", false);
		}
		return is;
	}

	@Override
	public boolean clearExpired(Date date) {
		Log.i("Cookies", "clearExpired");
		boolean clearedAny = false;
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

		for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			Cookie cookie = entry.getValue();
			if (cookie.isExpired(date)) {
				// Clear cookies from local store
				cookies.remove(name);
				// prefsWriter.putBoolean("Login", false);
				// Clear cookies from persistent store
				prefsWriter.remove(COOKIE_NAME_PREFIX + name);

				// We've cleared at least one
				clearedAny = true;
			}
		}

		// Update names in persistent store
		if (clearedAny) {
			prefsWriter.putString(COOKIE_NAME_STORE,
					TextUtils.join(",", cookies.keySet()));
		}
		prefsWriter.commit();

		return clearedAny;
	}

	@Override
	public List<Cookie> getCookies() {
		Log.i("Cookies", "getCookies");
		return new ArrayList<Cookie>(cookies.values());
	}

	/**
	 * Will make PersistentCookieStore instance ignore Cookies, which are
	 * non-persistent by signature (`Cookie.isPersistent`)
	 * 
	 * @param omitNonPersistentCookies
	 *            true if non-persistent cookies should be omited
	 */
	public void setOmitNonPersistentCookies(boolean omitNonPersistentCookies) {
		this.omitNonPersistentCookies = omitNonPersistentCookies;
	}

	/**
	 * Non-standard helper method, to delete cookie
	 * 
	 * @param cookie
	 *            cookie to be removed
	 */
	public void deleteCookie(Cookie cookie) {
		Log.i("Cookies", "deleteCookie");
		String name = cookie.getName();
		cookies.remove(name);
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		prefsWriter.remove(COOKIE_NAME_PREFIX + name);
		prefsWriter.commit();
	}

	/**
	 * Serializes Cookie object into String
	 * 
	 * @param cookie
	 *            cookie to be encoded, can be null
	 * @return cookie encoded as String
	 */
	protected String encodeCookie(SerializableCookie cookie) {
		if (cookie == null)
			return null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(os);
			outputStream.writeObject(cookie);
		} catch (Exception e) {
			return null;
		}

		return byteArrayToHexString(os.toByteArray());
	}

	/**
	 * Returns cookie decoded from cookie string
	 * 
	 * @param cookieString
	 *            string of cookie as returned from http request
	 * @return decoded cookie or null if exception occured
	 */
	protected Cookie decodeCookie(String cookieString) {
		byte[] bytes = hexStringToByteArray(cookieString);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		Cookie cookie = null;
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			cookie = ((SerializableCookie) objectInputStream.readObject())
					.getCookie();
		} catch (Exception exception) {
			Log.d(LOG_TAG, "decodeCookie failed", exception);
		}

		return cookie;
	}

	/**
	 * Using some super basic byte array <-> hex conversions so we don't have to
	 * rely on any large Base64 libraries. Can be overridden if you like!
	 * 
	 * @param bytes
	 *            byte array to be converted
	 * @return string containing hex values
	 */
	protected String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte element : bytes) {
			int v = element & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase(Locale.US);
	}

	/**
	 * Converts hex values from strings to byte arra
	 * 
	 * @param hexString
	 *            string of hex-encoded values
	 * @return decoded byte array
	 */
	protected byte[] hexStringToByteArray(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
					.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}
}
