const { remote } = require('webdriverio');
const assert = require("assert");


var options = {
    hostname: 'host.docker.internal',
    port: 4444,
    protocol: 'http',
    path: '/wd/hub',
    capabilities: {
        browserName: 'chrome',
        browserVersion: '80.0_VNC',
        'selenoid:options': {
            enableVideo: false,
            enableVNC: false
        }
    }
    //capabilities: { browserName: 'chrome' }
};
//var browser = webdriverio.remote(options);

describe("Todo Test", function() {
    let browser;

    beforeEach(async () => {
        browser = await remote(options);
        console.log('Navigating to the URL...');
        await browser.navigateTo('http://app:8080/todo');
    });

    afterEach(async () => {
        await browser.deleteSession();
    });

    it("should create a new task", async () => {
        const title = await browser.getTitle();
        assert.equal(title, 'Todo List');

        const searchInput = await browser.$('input[name="todoTask"]');
        await searchInput.waitForExist({ timeout: 10000 });
        await searchInput.setValue('New Task');
        await searchInput.click();

        const submitButton = await browser.$('button[type=submit]');
        await submitButton.click();

        const element = await browser.$('td*=New Task');
        await element.waitForExist({ timeout: 10000 });
        const eText = await element.getText();
        assert.equal(eText, 'New Task');
    }, 160000);

    it("should delete an existing task", async () => {
        const deleteButton = await browser.$(`td[name="New Task"] a[href*="/todo/delete/"]`);
        await deleteButton.waitForExist({ timeout: 10000 });
        await deleteButton.click();
    }, 160000);
});