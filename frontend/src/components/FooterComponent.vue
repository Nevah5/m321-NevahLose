<template>
  <footer>
    <div class="divider"></div>
    <div class="footer-bottom">
      <ul id="footer-nav">
        <li>
          <a href="https://github.com/Nevah5/m321-api-aces/">GitHub</a>
        </li>
        <li>
          <router-link to="/privacy">Privacy Policy</router-link>
        </li>
        <li>
          <router-link to="/tos">Terms of Use</router-link>
        </li>
      </ul>
      <span>&copy; 2024 API Aces - Nevah5</span>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
footer {
  color: #fff;
  text-align: center;
  padding: 10px;
  bottom: 0;
  width: 100%;

  .divider {
    height: 1px;
    background-color: var(--vt-c-stratos);
    margin: 10px 0;
  }

  .footer-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;

    #footer-nav {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      padding: 0;

      li {
        list-style-type: none;

        &:not(:last-child)::after {
          content: "●";
          font-size: 0.6rem;
          margin: 0 10px;
        }
      }
    }
  }
}
</style>
