package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 試験実施機能
 * ケース13
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms");
		getEvidence(new Object() {},"test01");
		String pageTitle = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		//DOM構築前に検索するとエラーになるため、表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		WebElement courseDetail = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("ol.breadcrumb li.active")
						)
				);
		getEvidence(new Object() {},"test02");

		assertEquals("コース詳細", courseDetail.getText());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		WebElement detailBtn = webDriver.findElement(
				By.xpath(
						"//span[text()='試験有']/parent::td/following-sibling::td//input[@type='submit']"));

		detailBtn.click();

		getEvidence(new Object() {},"test03");

		assertTrue(webDriver.getTitle().contains("セクション詳細"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {

	    WebElement detailBtn = webDriver.findElement(
	        By.xpath("//h3[text()='本日の試験']/following::table[1]//input[@type='submit']")
	    );

	    detailBtn.click();

	    getEvidence(new Object() {}, "test04");

	    assertTrue(webDriver.getTitle().contains("試験"));
	}


	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		// TODO ここに追加
		WebElement examStart = webDriver.findElement(
			    By.xpath("//input[@type='submit' and @value='試験を開始する']")
			);

			// スクロールして確実に表示
			((JavascriptExecutor)webDriver).executeScript(
			    "arguments[0].scrollIntoView(true);", examStart
			);

			// JSクリック（物理クリック回避）
			((JavascriptExecutor)webDriver).executeScript(
			    "arguments[0].click();", examStart
			);

			getEvidence(new Object() {}, "test05");

			assertTrue(webDriver.getCurrentUrl().contains("question"));

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() throws InterruptedException {
		// TODO ここに追加
		WebElement goConfirmation = webDriver.findElement(
				By.cssSelector("input[type='submit'][value='確認画面へ進む']"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", goConfirmation);

		//クリック可能になるまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver,Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(goConfirmation));

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", goConfirmation);

		getEvidence(new Object() {},"test06");

		assertTrue(webDriver.getCurrentUrl().contains("answerCheck"));

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		// TODO ここに追加
		WebElement sendButton = webDriver.findElement(
				By.id("sendButton"));

		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", sendButton);

		//クリック可能になるまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver,Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(sendButton));

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", sendButton);

		WebDriverWait wait2 = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait2.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {},"test07");

		assertTrue(webDriver.getCurrentUrl().contains("exam/result"));


	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {

	    WebElement backBtn = webDriver.findElement(
	            By.cssSelector("input[type='submit'][value='戻る']")
	    );

	    // スクロールして確実に見える位置へ
	    ((JavascriptExecutor) webDriver).executeScript(
	            "arguments[0].scrollIntoView(true);", backBtn
	    );

	    // クリック可能になるまで待つ
	    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
	    wait.until(ExpectedConditions.elementToBeClickable(backBtn));

	    // JSで強制クリック（物理クリック誤爆防止）
	    ((JavascriptExecutor) webDriver).executeScript(
	            "arguments[0].click();", backBtn
	    );

	    // ★★★ ページ遷移が完了するまで待つ（これが最重要）★★★
	    new WebDriverWait(webDriver, Duration.ofSeconds(5))
	            .until(ExpectedConditions.urlContains("/exam/start"));

	    // 画面が完全に安定してからスクショ
	    getEvidence(new Object() {}, "test08");

	    // 「過去の試験結果」テーブル取得
	    WebElement resultTable = webDriver.findElement(
	            By.xpath("//h3[text()='過去の試験結果']/following-sibling::table[1]")
	    );

	    // 全行取得
	    List<WebElement> rows = resultTable.findElements(By.xpath(".//tbody/tr"));
	    WebElement latestRow = rows.get(rows.size() - 1);

	    // 列取得
	    List<WebElement> cols = latestRow.findElements(By.tagName("td"));

	    // ▼ 点数チェック（例：0点, 0.0点 両対応）
	    String scoreText = cols.get(1).getText().replace("点", "").trim();
	    double score = Double.parseDouble(scoreText);
	    assertEquals(0.0, score, 0.1);

	    // ▼ 実施日時チェック（今日と一致すること）
	    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
	    assertTrue(cols.get(3).getText().contains(today));
	}


}
