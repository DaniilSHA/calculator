const functionFirstInput = document.querySelector(".first-function-input")
const functionSecondInput = document.querySelector(".second-function-input")
const iterationsInput = document.querySelector(".iterations-input")
const orderInput = document.querySelector(".order-input")
const reportButton = document.querySelector(".get-report-button")
const resetButton = document.querySelector(".reset-report-button")
const reportSticker = document.querySelector(".report-sticker")

let functionFirstValue
let functionSecondValue
let iterationsValue
let orderValue


document.querySelector(".get-report-button").addEventListener("click", () => {

    reportButton.setAttribute("disabled", "")
    reportSticker.innerHTML=''

    functionFirstValue = functionFirstInput.value;
    functionSecondValue = functionSecondInput.value;
    iterationsValue = iterationsInput.value;
    orderValue = orderInput.value;

    fetch("http://localhost:9090/calculate", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstFunction: functionFirstValue,
            secondFunction: functionSecondValue,
            itr: iterationsValue,
            order: orderValue
        })
    }).then((response) => {
        let stream = response.body.getReader()
        stream.read().then(function process({done, value}) {
            if (done) {
                console.log("Stream completed")
                reportButton.removeAttribute("disabled")
                return
            }
            render(value)
            return stream.read().then(process);
        })
    })
})

resetButton.addEventListener(("click"), () => {
    functionFirstInput.value = ''
    functionSecondInput.value = ''
    iterationsInput.value = ''
    orderInput.value = 'unordered'
    reportSticker.innerHTML=''
})


function render(value) {
    let str = new TextDecoder().decode(value);

    let newElement = document.createElement("div")
    newElement.classList.add("report-element")
    newElement.innerText = str
    reportSticker.appendChild(newElement)

    console.log(str)
}
