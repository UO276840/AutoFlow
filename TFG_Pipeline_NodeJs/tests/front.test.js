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

describe("Todo Test ", function() {
    it("Create Task and delete it", async ()=> {
const browser = await remote(options);
    try {
        console.log('Navigating to the URL...');
        await browser.navigateTo('http://app:8080/todo'); // Usar el nombre del servicio
        const title = await browser.getTitle()
        assert.equal(title,'Todo List');

        const searchInput = await browser.$('input[name="todoTask"]');
        await searchInput.waitForExist({timeout: 10000}); // Espera hasta 10 segundos
        await searchInput.setValue('New Task');
        await searchInput.click();

        const submitButton = await browser.$('button[type=submit]');
        await submitButton.click();

        const element = await browser.$('td*=New Task');
        await element.waitForExist({timeout: 10000});
        const eText = await element.getText();
        assert.equal(eText,'New Task')
        // Encuentra todos los enlaces "Eliminar"
        const deleteButtons = await browser.$(`td[name="New Task"] a[href*="/todo/delete/"]`);
        await deleteButtons.click();
    }finally {
        await browser.deleteSession()
    }
    },160000);
});