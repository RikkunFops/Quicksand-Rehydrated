class NavBar extends HTMLElement {
    connectedCallback() {
        if (this.childElementCount) return;
        this.innerHTML = `
        <nav>
        <span id="title">Quicksand rehydrated wiki</span>
        <span class="right">
            <span id="blocks">Blocks</span> 
            <span id="items">Items</span> 
            <span id="mobs">Mobs</span> 
            <span id="misc">Misc</span>
        </span>
        </nav>`;
    }
}
customElements.define('nav-bar', NavBar);
const blockListHTML = 
`
    <a href="blocks/mud.html">Mud block</a>
`;
const itemListHTML =
`
    <a href="items/example_item.html">Example Item</a><br>
    <a href="items/example_item_2.html">Example Item 2</a>
`;
const mobListHTML =
`
    <a href="mobs/example_mob.html">Example Mob</a>
`;
const miscListHTML =
`
    <a href="misc/example_misc.html">Example Misc</a>
`;
let blocksButton;
let itemsButton;
let mobsButton;
let list;

document.addEventListener("DOMContentLoaded", () => {
    blocksButton = document.getElementById("blocks");
    itemsButton = document.getElementById("items");
    mobsButton = document.getElementById("mobs");
    miscButton = document.getElementById("misc");
    list = document.getElementById("list");
    blocksButton.addEventListener("click", () => {
        list.innerHTML = blockListHTML;
        list.classList.add("is-open");
    });
    itemsButton.addEventListener("click", () => {
        list.innerHTML = itemListHTML;
        list.classList.add("is-open");
    });
    mobsButton.addEventListener("click", () => {
        list.innerHTML = mobListHTML;
        list.classList.add("is-open");
    });
    miscButton.addEventListener("click", () => {
        list.innerHTML = miscListHTML;
        list.classList.add("is-open");
    });

});