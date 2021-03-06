const functionFirstInput = document.querySelector(".first-function-input")
const functionSecondInput = document.querySelector(".second-function-input")
const iterationsInput = document.querySelector(".iterations-input")
const orderInput = document.querySelector(".order-input")
const reportButton = document.querySelector(".get-report-button")
const resetButton = document.querySelector(".reset-report-button")
const reportTable = document.querySelector(".report-table")

let functionFirstValue
let functionSecondValue
let iterationsValue
let orderValue


document.querySelector(".get-report-button").addEventListener("click", () => {

    reportButton.setAttribute("disabled", "")
    reportTable.innerHTML=''

    functionFirstValue = functionFirstInput.value;
    functionSecondValue = functionSecondInput.value;
    iterationsValue = iterationsInput.value;
    orderValue = orderInput.value;

    renderHeader(orderValue)

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
            renderCurrentReportMsg(orderValue, value)
            window.scrollTo(0, window.outerHeight);
            return stream.read().then(process);
        })
    })
})

resetButton.addEventListener(("click"), () => {
    functionFirstInput.value = ''
    functionSecondInput.value = ''
    iterationsInput.value = ''
    orderInput.value = 'unordered'
    reportTable.innerHTML=''
})


function renderCurrentReportMsg(order, value) {

    if (order === 'unordered') {

        const commonMsg = new TextDecoder().decode(value);

        for (let msg of commonMsg.split("\n\n\n")) {
            const partsOfMsg = msg.slice(msg.indexOf('[') + 1, msg.indexOf(']')).split(";");

            const currentRow = document.createElement("tr");
            const firstColumn = document.createElement("td");
            const secondColumn = document.createElement("td");
            const thirdColumn = document.createElement("td");
            const forthColumn = document.createElement("td");

            firstColumn.innerText = partsOfMsg[0]
            secondColumn.innerText = partsOfMsg[1] === undefined ? '' : partsOfMsg[1]
            thirdColumn.innerText = partsOfMsg[2] === undefined ? '' : partsOfMsg[2]
            forthColumn.innerText = partsOfMsg[3] === undefined ? '' : partsOfMsg[3]

            currentRow.appendChild(firstColumn)
            currentRow.appendChild(secondColumn)
            currentRow.appendChild(thirdColumn)
            currentRow.appendChild(forthColumn)
            reportTable.appendChild(currentRow)
        }
    }

    if (order === 'ordered') {

        const commonMsg = new TextDecoder().decode(value);

        for (let msg of commonMsg.split("\n\n\n")) {
            const partsOfMsg = msg.slice(msg.indexOf('[') + 1, msg.indexOf(']')).split(";");

            const currentRow = document.createElement("tr");
            const firstColumn = document.createElement("td");
            const secondColumn = document.createElement("td");
            const thirdColumn = document.createElement("td");
            const forthColumn = document.createElement("td");
            const fifthColumn = document.createElement("td");
            const sixthColumn = document.createElement("td");
            const seventhColumn = document.createElement("td");

            firstColumn.innerText = partsOfMsg[0] === undefined ? '' : partsOfMsg[0]
            secondColumn.innerText = partsOfMsg[1] === undefined ? '' : partsOfMsg[1]
            thirdColumn.innerText = partsOfMsg[2] === undefined ? '' : partsOfMsg[2]
            forthColumn.innerText = partsOfMsg[3] === undefined ? '' : partsOfMsg[3]
            fifthColumn.innerText = partsOfMsg[4] === undefined ? '' : partsOfMsg[4]
            sixthColumn.innerText = partsOfMsg[5] === undefined ? '' : partsOfMsg[5]
            seventhColumn.innerText = partsOfMsg[6] === undefined ? '' : partsOfMsg[6]

            currentRow.appendChild(firstColumn)
            currentRow.appendChild(secondColumn)
            currentRow.appendChild(thirdColumn)
            currentRow.appendChild(forthColumn)
            currentRow.appendChild(fifthColumn)
            currentRow.appendChild(sixthColumn)
            currentRow.appendChild(seventhColumn)
            reportTable.appendChild(currentRow)
        }
    }
}

function renderHeader(order) {

    if (order === 'unordered') {

        const headerRow = document.createElement("tr");
        const firstHeader = document.createElement("th");
        const secondHeader = document.createElement("th");
        const thirdHeader = document.createElement("th");
        const forthHeader = document.createElement("th");

        firstHeader.innerText = 'Iteration #'
        secondHeader.innerText = 'Function #'
        thirdHeader.innerText = 'Result'
        forthHeader.innerText = 'Calculation time'

        firstHeader.setAttribute("width", "10%")
        secondHeader.setAttribute("width", "10%")
        thirdHeader.setAttribute("width", "70%")
        forthHeader.setAttribute("width", "10%")

        headerRow.appendChild(firstHeader)
        headerRow.appendChild(secondHeader)
        headerRow.appendChild(thirdHeader)
        headerRow.appendChild(forthHeader)
        reportTable.appendChild(headerRow)

    }

    if (order === 'ordered') {

        const headerRow = document.createElement("tr");
        const firstHeader = document.createElement("th");
        const secondHeader = document.createElement("th");
        const thirdHeader = document.createElement("th");
        const forthHeader = document.createElement("th");
        const fifthHeader = document.createElement("th");
        const sixthHeader = document.createElement("th");
        const seventhHeader = document.createElement("th");

        firstHeader.innerText = 'Iteration #'
        secondHeader.innerText = 'Result of function #1'
        thirdHeader.innerText = 'Calculation time of function #1'
        forthHeader.innerText = 'Future results count of function #1'
        fifthHeader.innerText = 'Result of function #2'
        sixthHeader.innerText = 'Calculation time of function #2'
        seventhHeader.innerText = 'Future results count of function #2'

        firstHeader.setAttribute("width", "10%")
        secondHeader.setAttribute("width", "20%")
        thirdHeader.setAttribute("width", "10%")
        forthHeader.setAttribute("width", "10%")
        fifthHeader.setAttribute("width", "20%")
        sixthHeader.setAttribute("width", "10%")
        seventhHeader.setAttribute("width", "10%")

        headerRow.appendChild(firstHeader)
        headerRow.appendChild(secondHeader)
        headerRow.appendChild(thirdHeader)
        headerRow.appendChild(forthHeader)
        headerRow.appendChild(fifthHeader)
        headerRow.appendChild(sixthHeader)
        headerRow.appendChild(seventhHeader)

        reportTable.appendChild(headerRow)

    }
}