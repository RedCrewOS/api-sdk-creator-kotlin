package au.com.redcrew.apisdkcreator.httpclient

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import kotlin.reflect.KClass

class UnstructuredDataTest : DescribeSpec({
    describe("unstructured data") {
        describe("UnstructuredDataToGenericTypeUnmarshaller") {
            it("should return result of unmarshalling") {
                val unmarshaller = object : UnstructuredDataToGenericTypeUnmarshaller() {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : Any> unmarshallString(cls: KClass<T>, data: String): Either<SdkError, T> =
                        Integer.parseInt(data).right() as Either<SdkError, T>
                }

                unmarshaller(Int::class)(UnstructuredData.String("1234")).shouldBeRight(1234)
            }

            it("should return error when unmarshalling fails") {
                val error = SdkError("fake-error", "Something went wrong")

                val unmarshaller = object : UnstructuredDataToGenericTypeUnmarshaller() {
                    override fun <T : Any> unmarshallString(cls: KClass<T>, data: String): Either<SdkError, T> =
                        error.left()
                }

                unmarshaller(Int::class)(UnstructuredData.String("1234")).shouldBeLeft(error)
            }
        }
    }
})
