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